

package com.smartcoding.schedule.core.thread;

import cn.hutool.core.collection.CollectionUtil;
import com.smartcoding.schedule.core.conf.XxlJobAdminConfig;
import com.smartcoding.schedule.core.enums.AddressTypeEnum;
import com.smartcoding.schedule.core.enums.ThreadTypeEnum;
import com.smartcoding.schedule.core.model.XxlJobGroup;
import com.smartcoding.schedule.core.model.XxlJobRegistry;
import com.smartcoding.schedule.core.util.StringBuilderUtil;
import com.smartcoding.job.core.enums.RegistryConfig;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * job registry instance
 *
 * @author xuxueli 2016-10-02 19:10:24
 */
public class JobRegistryMonitorHelper {
    private static Logger logger = LoggerFactory.getLogger(JobRegistryMonitorHelper.class);

    private static JobRegistryMonitorHelper instance = new JobRegistryMonitorHelper();

    public static JobRegistryMonitorHelper getInstance() {
        return instance;
    }

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2, new BasicThreadFactory.Builder().namingPattern("JobRegistryMonitorHelper").daemon(true).build());
    private ScheduledFuture<?> jobRegistryMonitorScheduledFuture;
    private AtomicBoolean monitorToStop = new AtomicBoolean(false);
    private Runnable jobRegistryMonitor;

    public JobRegistryMonitorHelper() {
        this.jobRegistryMonitor = this::loadRegistryAddressList;
    }

    public void start() {
        try {
            if (monitorToStop.compareAndSet(false, true)) {
                    jobRegistryMonitorScheduledFuture = executorService.scheduleAtFixedRate(jobRegistryMonitor, 2, RegistryConfig.BEAT_TIMEOUT, TimeUnit.SECONDS);
                logger.info(">>>>>>>>>>> xxl-job,Start JobRegistryMonitorHelper success!");
            }
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job,Start JobRegistryMonitorHelper failed!", t);
        }
    }

    private void loadRegistryAddressList() {
        try {
            if (!XxlJobAdminConfig.getAdminConfig().getXxlJobScheduleService().isOpenThread(ThreadTypeEnum.JOB_REGISTRY_MONITOR_HELPER)) {
                logger.warn(">>>>>>>>>>> xxl-job, job registry monitor thread is close");
                return;
            }
            // remove dead address (admin/executor)
            List<Integer> ids = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().findDead(RegistryConfig.DEAD_TIMEOUT);
            if (ids != null && ids.size() > 0) {
                XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().removeDead(ids);
            }
            // auto registry group
            List<XxlJobGroup> groupList = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().findByAddressType(AddressTypeEnum.AUTO.getCode());
            if (CollectionUtil.isNotEmpty(groupList)) {
                // fresh online address (admin/executor)
                Map<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
                List<XxlJobRegistry> list = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().findByRegistryGroup(RegistryConfig.DEAD_TIMEOUT, RegistryConfig.RegistType.EXECUTOR.name());
                if (CollectionUtil.isNotEmpty(list)) {
                    for (XxlJobRegistry item : list) {
                        String appName = item.getRegistryKey();
                        List<String> registryList = appAddressMap.get(appName);
                        if (registryList == null) {
                            registryList = new ArrayList<String>();
                        }
                        if (!registryList.contains(item.getRegistryValue())) {
                            registryList.add(item.getRegistryValue());
                        }
                        appAddressMap.put(appName, registryList);
                    }
                }

                // fresh group address
                for (XxlJobGroup group : groupList) {
                    List<String> registryList = appAddressMap.get(group.getAppName());
                    String addressListStr = StringBuilderUtil.listToString(registryList);
                    XxlJobGroup xxlJobGroup = new XxlJobGroup();
                    xxlJobGroup.setId(group.getId());
                    xxlJobGroup.setAddressList(addressListStr);
                    XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().updateByPrimaryKeySelective(xxlJobGroup);
                }
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> xxl-job, job registry jobRegistryMonitor thread error:{}", e);
        }
    }

    public void toStop() {
        try {
            logger.info(">>>>>>>>>>> xxl-job, JobRegistryMonitorHelper stopping");
            if (monitorToStop.compareAndSet(true, false)&&jobRegistryMonitorScheduledFuture!=null) {
                jobRegistryMonitorScheduledFuture.cancel(true);
                logger.info(">>>>>>>>>>> xxl-job,Stop JobRegistryMonitorHelper success!");
            }
            XxlJobAdminConfig.getAdminConfig().getXxlJobScheduleService().autoDeleteOpenThread(ThreadTypeEnum.JOB_REGISTRY_MONITOR_HELPER);
            executorService.shutdown();
        } catch (Exception t) {
            logger.error(">>>>>>>>>>> xxl-job, stop JobRegistryMonitorHelper   failed!", t);
        }
    }

}
