package cn.smartcoding.job.core.thread;

import cn.smartcoding.job.core.util.FileUtil;
import cn.smartcoding.job.core.log.XxlJobFileAppender;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * job file clean thread
 *
 * @author xuxueli 2017-12-29 16:23:43
 */
public class JobLogFileCleanThread {
    private static Logger logger = LoggerFactory.getLogger(JobLogFileCleanThread.class);

    private static JobLogFileCleanThread instance = new JobLogFileCleanThread();
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static JobLogFileCleanThread getInstance() {
        return instance;
    }

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2, new BasicThreadFactory.Builder().namingPattern("JobLogFileCleanThread").daemon(true).build());
    private ScheduledFuture<?> fileCleanScheduledFuture;
    private AtomicBoolean fileCleanToStop = new AtomicBoolean(false);

    public void start(final long logRetentionDays) {
        Runnable jobLogFileCleanThread = () -> {
            // limit min value
            if (logRetentionDays < 3) {
                return;
            }
            try {
                // clean log dir, over logRetentionDays
                File[] childDirs = new File(XxlJobFileAppender.getLogPath()).listFiles();
                if (childDirs != null && childDirs.length > 0) {

                    // today
                    Calendar todayCal = Calendar.getInstance();
                    todayCal.set(Calendar.HOUR_OF_DAY, 0);
                    todayCal.set(Calendar.MINUTE, 0);
                    todayCal.set(Calendar.SECOND, 0);
                    todayCal.set(Calendar.MILLISECOND, 0);

                    Date todayDate = todayCal.getTime();

                    for (File childFile : childDirs) {

                        // valid
                        if (!childFile.isDirectory()) {
                            continue;
                        }
                        if (!childFile.getName().contains("-")) {
                            continue;
                        }

                        // file create date
                        Date logFileCreateDate = null;
                        try {
                            logFileCreateDate = SIMPLE_DATE_FORMAT.parse(childFile.getName());
                        } catch (ParseException e) {
                            logger.error(e.getMessage(), e);
                        }
                        if (logFileCreateDate == null) {
                            continue;
                        }

                        if ((todayDate.getTime() - logFileCreateDate.getTime()) >= logRetentionDays * (24 * 60 * 60 * 1000)) {
                            FileUtil.deleteRecursively(childFile);
                        }

                    }
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);

            }
        };

        try {
            if (fileCleanToStop.compareAndSet(false, true)) {
                fileCleanScheduledFuture = executorService.scheduleAtFixedRate(jobLogFileCleanThread, 1, 1, TimeUnit.DAYS);
                logger.info(">>>>>>>>>>> xxl-job,Start JobLogFileCleanThread success!");
            }
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,Start JobLogFileCleanThread failed!", t);
        }
    }

    public void toStop() {
        try {
            logger.info(">>>>>>>>>>> xxl-job, JobLogFileCleanThread  stopping");
            if (fileCleanToStop.compareAndSet(true, false) && fileCleanScheduledFuture != null) {
                fileCleanScheduledFuture.cancel(true);
                logger.info(">>>>>>>>>>> xxl-job,stop JobLogFileCleanThread  success!");
            }
            executorService.shutdown();
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,stop JobLogFileCleanThread   failed!", t);
        }
    }

}
