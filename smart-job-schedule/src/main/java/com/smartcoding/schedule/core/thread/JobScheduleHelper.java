

package com.smartcoding.schedule.core.thread;

import cn.hutool.core.collection.CollectionUtil;
import com.smartcoding.schedule.core.conf.XxlJobAdminConfig;
import com.smartcoding.schedule.core.cron.CronExpression;
import com.smartcoding.schedule.core.enums.ThreadTypeEnum;
import com.smartcoding.schedule.core.model.XxlJobInfo;
import com.smartcoding.schedule.core.trigger.TriggerStatusEnum;
import com.smartcoding.schedule.core.trigger.TriggerTypeEnum;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xuxueli 2019-05-21
 */
public class JobScheduleHelper {
    private static Logger logger = LoggerFactory.getLogger(JobScheduleHelper.class);

    private static JobScheduleHelper instance = new JobScheduleHelper();

    public static JobScheduleHelper getInstance() {
        return instance;
    }

    private static String LOCK_SQL = "select * from xxl_job_lock where lock_name = 'schedule_lock' for update";
    // pre read
    public static final long PRE_READ_MS = 5000;
    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2, new BasicThreadFactory.Builder().namingPattern("JobScheduleHelper").daemon(true).build());
    private ScheduledFuture<?> scheduleThreadScheduledFuture;
    private ScheduledFuture<?> ringThreadScheduledFuture;
    private AtomicBoolean scheduleThreadToStop = new AtomicBoolean(false);
    private AtomicBoolean ringThreadToStop = new AtomicBoolean(false);
    private volatile static Map<Integer, List<Long>> ringData = new ConcurrentHashMap<>();

    private AtomicBoolean jobScheduleThread = new AtomicBoolean(true);
    private Runnable scheduleThread;
    private Runnable ringThread;

    public JobScheduleHelper() {

        this.scheduleThread = this::scanJobSchedule;
        this.ringThread = this::runTriggerJob;
    }

    public void start() {
        try {
            if (scheduleThreadToStop.compareAndSet(false, true)) {
                if (scheduleThreadScheduledFuture == null) {
                    scheduleThreadScheduledFuture = executorService.scheduleAtFixedRate(scheduleThread, 1000, 1000, TimeUnit.MILLISECONDS);
                }
                logger.info(">>>>>>>>>>> xxl-job,Start JobScheduleHelper scheduleThread  success!");
            }
            if (ringThreadToStop.compareAndSet(false, true)) {
                if (ringThreadScheduledFuture == null) {
                    ringThreadScheduledFuture = executorService.scheduleAtFixedRate(ringThread, 1000, 1000, TimeUnit.MILLISECONDS);
                }
                logger.info(">>>>>>>>>>> xxl-job,Start JobScheduleHelper  ringThread  success!");
            }
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,Start Job pull machine failed!", t);
        }
    }

    private void scanJobSchedule() {
        if (!XxlJobAdminConfig.getAdminConfig().getXxlJobScheduleService().isOpenThread(ThreadTypeEnum.JOB_SCHEDULE_HELPER)) {
            jobScheduleThread.compareAndSet(true, false);
            logger.warn(">>>>>>>>>>> xxl-job, job schedule scan job thread is close");
            return;
        }
        if (!jobScheduleThread.get()) {
            jobScheduleThread.set(true);
        }
        Connection conn = null;
        // 扫描任务
        PreparedStatement preparedStatement = null;
        // pre-read count: treadpool-size * trigger-qps (each trigger cost 50ms, qps = 1000/50 = 20)
        int preReadCount = (XxlJobAdminConfig.getAdminConfig().getTriggerPoolFastMax() + XxlJobAdminConfig.getAdminConfig().getTriggerPoolSlowMax()) * 20;

        try {
            if (conn == null || conn.isClosed()) {
                conn = XxlJobAdminConfig.getAdminConfig().getDataSource().getConnection();
            }
            conn.setAutoCommit(false);

            preparedStatement = conn.prepareStatement(LOCK_SQL);
            preparedStatement.execute();
            // tx start

            // 1、预读5s内调度任务
            long nowTime = System.currentTimeMillis();
            List<XxlJobInfo> scheduleList = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().scheduleJobQuery(nowTime + PRE_READ_MS, preReadCount);
            if (CollectionUtil.isEmpty(scheduleList)) {
                return;
            }
            // 2、推送时间轮
            for (XxlJobInfo jobInfo : scheduleList) {
                // 时间轮刻度计算
                if (nowTime > jobInfo.getTriggerNextTime() + PRE_READ_MS) {
                    // 过期超5s：本地忽略，当前时间开始计算下次触发时间
                    // fresh next
                    Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(new Date());
                    setJobInfo(jobInfo, nextValidTime);
                } else if (nowTime > jobInfo.getTriggerNextTime()) {
                    // 过期5s内 ：立即触发一次，当前时间开始计算下次触发时间；
                    CronExpression cronExpression = new CronExpression(jobInfo.getJobCron());
                    long nextTime = cronExpression.getNextValidTimeAfter(new Date()).getTime();
                    // 1、trigger
                    JobTriggerPoolHelper.trigger(jobInfo.getId(), TriggerTypeEnum.CRON, -1, null, null);
                    logger.debug(">>>>>>>>>>> xxl-job, schedule push trigger : jobId = " + jobInfo.getId());

                    // 2、fresh next
                    jobInfo.setTriggerLastTime(jobInfo.getTriggerNextTime());
                    jobInfo.setTriggerNextTime(nextTime);

                    // 下次5s内：预读一次；
                    if (jobInfo.getTriggerNextTime() - nowTime < PRE_READ_MS) {

                        // 1、make ring second
                        int ringSecond = (int) ((jobInfo.getTriggerNextTime() / 1000) % 60);

                        // 2、push time ring
                        pushTimeRing(ringSecond, jobInfo.getId());

                        // 3、fresh next
                        Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(new Date(jobInfo.getTriggerNextTime()));
                        setJobInfo(jobInfo, nextValidTime);
                    }

                } else {
                    // 未过期：正常触发，递增计算下次触发时间

                    // 1、make ring second
                    int ringSecond = (int) ((jobInfo.getTriggerNextTime() / 1000) % 60);

                    // 2、push time ring
                    pushTimeRing(ringSecond, jobInfo.getId());

                    // 3、fresh next
                    Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(new Date(jobInfo.getTriggerNextTime()));
                    setJobInfo(jobInfo, nextValidTime);
                }
            }
            // 3、更新trigger信息
            for (XxlJobInfo jobInfo : scheduleList) {
                XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().scheduleUpdate(jobInfo);
            }

            // tx stop
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> xxl-job, JobScheduleHelper#scheduleThread error:{}", e);
        } finally {
            // commit
            try {
                conn.commit();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
            if (null != preparedStatement) {
                try {
                    preparedStatement.close();
                } catch (SQLException ignore) {
                    logger.error(ignore.getMessage(), ignore);
                }
            }
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void setJobInfo(XxlJobInfo jobInfo, Date nextValidTime) {
        if (nextValidTime != null) {
            jobInfo.setTriggerLastTime(jobInfo.getTriggerNextTime());
            jobInfo.setTriggerNextTime(nextValidTime.getTime());
        } else {
            jobInfo.setTriggerStatus(TriggerStatusEnum.STOPPING.getCode());
            jobInfo.setTriggerLastTime(0L);
            jobInfo.setTriggerNextTime(0L);
        }
    }

    private void runTriggerJob() {
        try {
            if (!jobScheduleThread.get()) {
                logger.warn(">>>>>>>>>>> xxl-job, job schedule run trigger job monitor thread is close");
            }
            // second data
            List<Long> ringItemData = new ArrayList<>();
            // 避免处理耗时太长，跨过刻度，向前校验一个刻度；
            int nowSecond = LocalTime.now().getSecond();
            for (int i = 0; i < 2; i++) {
                List<Long> tmpData = ringData.remove((nowSecond + 60 - i) % 60);
                if (tmpData != null) {
                    ringItemData.addAll(tmpData);
                }
            }

            // ring trigger
            logger.info(">>>>>>>>>>> xxl-job, time-ring beat : " + nowSecond + " = " + ringItemData);
            if (ringItemData.size() > 0) {
                // do trigger
                for (Long jobId : ringItemData) {
                    // do trigger
                    JobTriggerPoolHelper.trigger(jobId, TriggerTypeEnum.CRON, -1, null, null);
                }
                // clear
                ringItemData.clear();
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> xxl-job, JobScheduleHelper#ringThread error:{}", e);
        }
    }

    private void pushTimeRing(int ringSecond, Long jobId) {
        // push async ring
        List<Long> ringItemData = ringData.get(ringSecond);
        if (ringItemData == null) {
            ringItemData = new ArrayList<Long>();
            ringData.put(ringSecond, ringItemData);
        }
        ringItemData.add(jobId);

        logger.info(">>>>>>>>>>> xxl-job, schedule push time-ring : " + ringSecond + " = " + ringItemData);
    }

    public void toStop() {
        try {
            logger.info(">>>>>>>>>>> xxl-job, JobScheduleHelper stopping");
            if (scheduleThreadToStop.compareAndSet(true, false)) {
                scheduleThreadScheduledFuture.cancel(true);
                logger.info(">>>>>>>>>>> xxl-job, Stop JobScheduleHelper scheduleThread success!");
            }
            long start = System.currentTimeMillis();
            long waitJobRunnerTime = 60 * 1000;
            while (ringData.size() > 0 && System.currentTimeMillis() - start > waitJobRunnerTime) {
                for (Integer second : ringData.keySet()) {
                    List<Long> tmpData = ringData.get(second);
                    if (tmpData != null && tmpData.size() > 0) {
                        continue;
                    }
                }
                TimeUnit.SECONDS.sleep(3);
            }
            if (ringThreadToStop.compareAndSet(true, false)) {
                ringThreadScheduledFuture.cancel(true);
                logger.info(">>>>>>>>>>> xxl-job,Stop JobScheduleHelper  ringThread success!");
            }
            XxlJobAdminConfig.getAdminConfig().getXxlJobScheduleService().autoDeleteOpenThread(ThreadTypeEnum.JOB_SCHEDULE_HELPER);
            executorService.shutdown();
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job, stop JobScheduleHelper  failed!", t);
        }
    }


}
