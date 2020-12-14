package cn.smartcoding.common.robot.util;//package com.smartcoding.robot.com.smartcoding.robot.core.util;
//
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.concurrent.*;
//
///**
// * @author abao
// * @date 15/10/2018
// * <p>
// * 线程池相关服务
// */
//public class ThreadSupport {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadSupport.class);
//
//    public static ThreadPoolExecutor newSingleExecutor(String prefix) {
//        return new ThreadPoolExecutor(1, 1,
//                1000L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>()
//                , newThreadFactory(prefix));
//    }
//
//    /**
//     * 与newCachedThreadPool的线程池
//     *
//     * @param namePrefix
//     * @param maxThread    最大线程数量，超过的话，后续提交任务报错
//     * @param aliveSeconds 线程空闲存活时间，以秒计时
//     * @return
//     */
//    public static ThreadPoolExecutor newFiniteIOExecutor(String namePrefix, int maxThread, int aliveSeconds) {
//        BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
//        return newExecutor(namePrefix, 0, maxThread, aliveSeconds * 1000, workQueue, new ThreadPoolExecutor.AbortPolicy());
//    }
//
//    public static ThreadPoolExecutor newFiniteIOExecutor(String namePrefix, int maxThread, int aliveSeconds, RejectedExecutionHandler rejectedExecutionHandler) {
//        BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
//        return newExecutor(namePrefix, 0, maxThread, aliveSeconds * 1000, workQueue, rejectedExecutionHandler);
//    }
//
//    /**
//     * 新起固定线程池，结束即释放资源
//     *
//     * @param namePrefix
//     * @param maxThread
//     * @param aliveSeconds
//     * @return
//     */
//    public static ThreadPoolExecutor newFixedCachedThreadExecutor(String namePrefix, int maxThread, int aliveSeconds) {
//        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
//        ThreadPoolExecutor executor = newExecutor(namePrefix, maxThread, maxThread, aliveSeconds * 1000, workQueue, new ThreadPoolExecutor.AbortPolicy());
//        executor.allowCoreThreadTimeOut(true);
//        return executor;
//    }
//
//    public static ScheduledExecutorService newScheduledThreadPoolExecutor(String namePrefix, int coreNum, int aliveSeconds) {
//        ThreadFactory factory = newThreadFactory(namePrefix);
//        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
//        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(coreNum, factory, handler);
//        executor.setKeepAliveTime(aliveSeconds, TimeUnit.SECONDS);
//        executor.allowCoreThreadTimeOut(true);
//        return executor;
//    }
//
//    public static void awaitFutures(List<Future> futures) {
//        for (Future future : futures) {
//            try {
//                future.get();
//            } catch (Exception e) {
//                // nothing
//            }
//        }
//    }
//
//    public static ThreadPoolExecutor newExecutor(String namePrefix, int coreThread, int maxThread, long keepAliveTime, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
//        ThreadFactory factory = newThreadFactory(namePrefix);
//
//        return new ThreadPoolExecutor(coreThread, maxThread,
//                keepAliveTime, TimeUnit.MILLISECONDS,
//                workQueue, factory, handler);
//
//    }
//
//    public static ThreadFactory newThreadFactory(String namePrefix) {
//        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
//        builder.setNameFormat(namePrefix + "-%d");
//        builder.setUncaughtExceptionHandler((t, ex) -> {
//            System.err.printf("thread uncaughtExceptionHandler thread: %s id: %d error:%s", t.getName(), t.getId(), ex.getMessage());
//            LOGGER.error("thread uncaughtExceptionHandler thread: %{} id: %{} error:{}", t.getName(), t.getId(), ex.getMessage(), ex);
//            ex.printStackTrace();
//        });
//        ThreadFactory factory = builder.build();
//        return factory;
//    }
//}
