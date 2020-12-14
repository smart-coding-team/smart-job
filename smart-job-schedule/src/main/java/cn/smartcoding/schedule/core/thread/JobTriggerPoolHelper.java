

package cn.smartcoding.schedule.core.thread;

import cn.smartcoding.schedule.core.conf.XxlJobAdminConfig;
import cn.smartcoding.schedule.core.trigger.TriggerTypeEnum;
import cn.smartcoding.schedule.core.trigger.XxlJobTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * job trigger thread pool helper
 *
 * @author xuxueli 2018-07-03 21:08:07
 */
public class JobTriggerPoolHelper {
    private static Logger logger = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

    // fast/slow thread pool
    private ThreadPoolExecutor fastTriggerPool = null;
    private ThreadPoolExecutor slowTriggerPool = null;
    // ---------------------- trigger pool ----------------------

    public void start(){
        // fast/slow thread pool
          fastTriggerPool = new ThreadPoolExecutor(
                50,
                  XxlJobAdminConfig.getAdminConfig().getTriggerPoolFastMax(),
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "JobTriggerPoolHelper-fastTriggerPool-" + r.hashCode());
                    }
                });

          slowTriggerPool = new ThreadPoolExecutor(
                10,
                  XxlJobAdminConfig.getAdminConfig().getTriggerPoolSlowMax(),
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "JobTriggerPoolHelper-slowTriggerPool-" + r.hashCode());
                    }
                });
    }



    // job timeout count
    private volatile long minTim = System.currentTimeMillis()/60000;     // ms > min
    private volatile ConcurrentMap<Long, AtomicInteger> jobTimeoutCountMap = new ConcurrentHashMap<>();


    /**
     * add trigger
     */
    public void addTrigger(final Long jobId, final TriggerTypeEnum triggerType, final int failRetryCount, final String executorShardingParam, final String executorParam, final String address) {

        // choose thread pool
        ThreadPoolExecutor triggerPool = fastTriggerPool;
        AtomicInteger jobTimeoutCount = jobTimeoutCountMap.get(jobId);
        // job-timeout 10 times in 1 min
        if (jobTimeoutCount!=null && jobTimeoutCount.get() > 10) {
            triggerPool = slowTriggerPool;
        }

        // trigger
        triggerPool.execute(new Runnable() {
            @Override
            public void run() {

                long start = System.currentTimeMillis();

                try {
                    // do trigger
                    XxlJobTrigger.trigger(jobId, triggerType, failRetryCount, executorShardingParam, executorParam, address);
                } catch (Exception e) {
                    logger.error("trigger job error", e);
                } finally {
                    // check timeout-count-map
                    long minTimNow = System.currentTimeMillis()/60000;
                    if (minTim != minTimNow) {
                        minTim = minTimNow;
                        jobTimeoutCountMap.clear();
                    }

                    // incr timeout-count-map
                    long cost = System.currentTimeMillis()-start;
                    // ob-timeout threshold 500ms
                    if (cost > 500) {
                        AtomicInteger timeoutCount = jobTimeoutCountMap.putIfAbsent(jobId, new AtomicInteger(1));
                        if (timeoutCount != null) {
                            timeoutCount.incrementAndGet();
                        }
                    }

                }

            }
        });
    }

    public void stop() {
        //triggerPool.shutdown();
        fastTriggerPool.shutdownNow();
        slowTriggerPool.shutdownNow();
        logger.info(">>>>>>>>> xxl-job trigger thread pool shutdown success.");
    }

    // ---------------------- helper ----------------------

    private static JobTriggerPoolHelper helper = new JobTriggerPoolHelper();

    /**
     * @param jobId
     * @param triggerType
     * @param failRetryCount
 * 			>=0: use this param
 * 			<0: use param from job info config
     * @param executorShardingParam
     * @param executorParam
*          null: use job param
     * @param address
     */
    public static void trigger(Long jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam, String address) {
        helper.addTrigger(jobId, triggerType, failRetryCount, executorShardingParam, executorParam, address);
    }
    public static void trigger(Long jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam) {
        helper.addTrigger(jobId, triggerType, failRetryCount, executorShardingParam, executorParam, null);
    }
    public static void toStart() {
        helper.start();
    }
    public static void toStop() {
        helper.stop();
    }

}
