

package com.smartcoding.schedule.core.trigger;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.smartcoding.schedule.core.conf.XxlJobAdminConfig;
import com.smartcoding.schedule.core.conf.XxlJobScheduler;
import com.smartcoding.schedule.core.enums.AddressTypeEnum;
import com.smartcoding.schedule.core.enums.ExecutorWayTypeEnum;
import com.smartcoding.schedule.core.model.XxlJobGroup;
import com.smartcoding.schedule.core.model.XxlJobInfo;
import com.smartcoding.schedule.core.model.XxlJobLog;
import com.smartcoding.schedule.core.route.ExecutorRouteStrategyEnum;
import com.smartcoding.schedule.core.route.strategy.IdleBeat;
import com.smartcoding.schedule.core.util.I18nUtil;
import com.smartcoding.job.core.biz.ExecutorBiz;
import com.smartcoding.job.core.biz.model.ReturnT;
import com.smartcoding.job.core.biz.model.TriggerParam;
import com.smartcoding.job.core.enums.ExecutorBlockStrategyEnum;
import com.smartcoding.job.core.enums.RegistryConfig;
import com.google.common.collect.Lists;
import com.xxl.rpc.util.IpUtil;
import com.xxl.rpc.util.ThrowableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 任务触发器
 * xxl-job trigger
 * Created by xuxueli on 17/7/13.
 */
public class XxlJobTrigger {
    private static Logger logger = LoggerFactory.getLogger(XxlJobTrigger.class);

    /**
     * trigger job
     *
     * @param jobId
     * @param triggerType
     * @param failRetryCount        >=0: use this param
     *                              <0: use param from job info config
     * @param executorShardingParam
     * @param executorParam         null: use job param
     * @param address
     */
    public static void trigger(Long jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam, String address) {
        // load data
        XxlJobAdminConfig adminConfig = XxlJobAdminConfig.getAdminConfig();
        XxlJobInfo jobInfo = adminConfig.getXxlJobInfoDao().selectByPrimaryKey(jobId);
        if (jobInfo == null) {
            logger.warn(">>>>>>>>>>>> trigger fail, jobId invalid，jobId={}", jobId);
            return;
        }
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }
        int finalFailRetryCount = failRetryCount >= 0 ? failRetryCount : jobInfo.getExecutorFailRetryCount();
        XxlJobGroup group = adminConfig.getXxlJobGroupDao().selectByPrimaryKey(jobInfo.getJobGroup());

        // sharding param
        int[] shardingParam = null;
        if (executorShardingParam != null) {
            String[] shardingArr = executorShardingParam.split("/");
            if (shardingArr.length == 2 && isNumeric(shardingArr[0]) && isNumeric(shardingArr[1])) {
                shardingParam = new int[2];
                shardingParam[0] = Integer.valueOf(shardingArr[0]);
                shardingParam[1] = Integer.valueOf(shardingArr[1]);
            }
        }
        // job execute route strategy
        Integer addressType = group.getAddressType();
        //获取在线的注册地址，自动注册
        List<String> registryList = StrUtil.split(group.getAddressList(), ',', true, true);

        ExecutorWayTypeEnum executorWayTypeEnum = ExecutorWayTypeEnum.SYSTEM;
        //获取在线的注册地址,直接指定
        if (StrUtil.isNotEmpty(address)) {
            registryList = Lists.newArrayList(address);
        } else if (AddressTypeEnum.ADD.getCode().equals(addressType)) {
            //获取在线的注册地址，手动添加
            registryList = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().getOnLineAddressList(RegistryConfig.RegistType.EXECUTOR.name(), group.getAppName(), registryList);
        }
        if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST.equals(ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null))
                && registryList.size() > 0
                && shardingParam == null) {
            for (int i = 0; i < registryList.size(); i++) {
                processTrigger(jobInfo, finalFailRetryCount, triggerType, i, registryList.size(), registryList, addressType);
            }
        } else {
            if (shardingParam == null) {
                shardingParam = new int[]{0, 1};
            }
            processTrigger(jobInfo, finalFailRetryCount, triggerType, shardingParam[0], shardingParam[1], registryList, addressType);
        }

    }

    private static boolean isNumeric(String str) {
        try {
            long result = Long.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param jobInfo
     * @param finalFailRetryCount
     * @param triggerType
     * @param index               sharding index
     * @param total               sharding index
     * @param registryList
     * @param addressType
     */
    private static void processTrigger(XxlJobInfo jobInfo, int finalFailRetryCount, TriggerTypeEnum triggerType, int index, int total, List<String> registryList, Integer addressType) {

        // block strategy  param
        ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
        // route strategy
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);
        String shardingParam = (ExecutorRouteStrategyEnum.SHARDING_BROADCAST.equals(executorRouteStrategyEnum)) ? String.valueOf(index).concat("/").concat(String.valueOf(total)) : null;

        // 1、save log-id
        String jobRequestId = IdUtil.fastSimpleUUID().substring(0, 16);
        XxlJobLog jobLog = new XxlJobLog();
        jobLog.setJobGroup(jobInfo.getJobGroup());
        jobLog.setJobId(jobInfo.getId());
        jobLog.setJobName(jobInfo.getJobName());
        jobLog.setJobRequestId(jobRequestId);
        jobLog.setTriggerType(triggerType.getCode());
        Date triggerTime = new Date();
        String triggerAdminAddress = IpUtil.getIp();
        jobLog.setTriggerTime(triggerTime);
        jobLog.setTriggerAddress(triggerAdminAddress);
        XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().insertSelective(jobLog);
        logger.debug(">>>>>>>>>>> xxl-job trigger start, jobId:{},jobRequestId:{}", jobInfo.getId(), jobRequestId);

        // 2、init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setJobRequestId(jobRequestId);
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setLogId(jobLog.getId());
        triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
        triggerParam.setGlueType(jobInfo.getGlueType());
        triggerParam.setGlueSource(jobInfo.getGlueSource());
        Date glueUpdateTime = jobInfo.getGlueUpdatetime();

        triggerParam.setGlueUpdateTime(glueUpdateTime != null ? glueUpdateTime.getTime() : null);
        triggerParam.setBroadcastIndex(index);
        triggerParam.setBroadcastTotal(total);

        // 3、init address
        String address = null;
        ReturnT<String> routeAddressResult = null;
        if (registryList.size() > 0) {
            if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST.equals(executorRouteStrategyEnum)) {
                if (index < registryList.size()) {
                    address = registryList.get(index);
                } else {
                    address = registryList.get(0);
                }
            } else {
                routeAddressResult = executorRouteStrategyEnum.getRouter().route(triggerParam, registryList);
                if (routeAddressResult.getCode() == ReturnT.SUCCESS_CODE) {
                    address = routeAddressResult.getContent();
                }
            }
        } else {
            routeAddressResult = new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobconf_trigger_address_empty"));
        }

        // 4、trigger remote executor
        ReturnT<String> triggerResult = null;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
        } else {
            triggerResult = new ReturnT<String>(ReturnT.FAIL_CODE, null);
        }

        List<TriggerMsg> list = new ArrayList<>(12);
        list.add(TriggerMsg.build(TriggerConstant.TRIGGER_TYPE, triggerType.getTitle(), 1));
        list.add(TriggerMsg.build(TriggerConstant.TRIGGER_ADMIN_ADDRESS, triggerAdminAddress, 2));
        list.add(TriggerMsg.build(TriggerConstant.TRIGGER_EXE_REG_TYPE, AddressTypeEnum.fromCode(addressType, AddressTypeEnum.AUTO).getTitle(), 3));
        list.add(TriggerMsg.build(TriggerConstant.TRIGGER_EXE_REG_ADDRESS, registryList.toString(), 4));
        list.add(TriggerMsg.build(TriggerConstant.EXECUTOR_ROUTE_STRATEGY, executorRouteStrategyEnum.getTitle(), 5));
        list.add(TriggerMsg.build(TriggerConstant.EXECUTOR_BLOCK_STRATEGY, blockStrategy.getTitle(), 6));
        list.add(TriggerMsg.build(TriggerConstant.TIMEOUT, jobInfo.getExecutorTimeout() + "", 7));
        list.add(TriggerMsg.build(TriggerConstant.EXECUTOR_FAIL_RETRY_COUNT, finalFailRetryCount + "", 8));
        if (shardingParam != null) {
            list.add(TriggerMsg.build(TriggerConstant.SHARDING_PARAM, shardingParam, 9));
        }
        //心跳检查
        if (routeAddressResult != null &&StrUtil.isNotEmpty(routeAddressResult.getMsg())) {
            if (routeAddressResult.getMsg().startsWith("{")) {
                IdleBeat idleBeat = JSONUtil.toBean(routeAddressResult.getMsg(), IdleBeat.class);
                list.add(TriggerMsg.build(idleBeat.getTitle(), idleBeat.toJsonIgnoreTitle(), 50));
            } else {
                list.add(TriggerMsg.build("心跳检测", routeAddressResult.getMsg(), 50));
            }
        }
        //触发调度
        list.add(TriggerMsg.build(TriggerConstant.TRIGGER_RUN, triggerResult.getMsg(), 51));
        // 6、save log trigger-info
        XxlJobLog updateJobLog = new XxlJobLog();
        updateJobLog.setId(jobLog.getId());
        updateJobLog.setExecutorAddress(address);
        updateJobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        updateJobLog.setExecutorParam(jobInfo.getExecutorParam());
        updateJobLog.setExecutorShardingParam(shardingParam);
        updateJobLog.setExecutorFailRetryCount(finalFailRetryCount);
        updateJobLog.setTriggerCode(triggerResult.getCode());
        updateJobLog.setTriggerMsg(JSONUtil.toJsonStr(list));
        XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateByPrimaryKeySelective(updateJobLog);

        XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateJobStatusByTriggerCode(jobLog.getId(),triggerResult.getCode());

        XxlJobLog xxlJobLog = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().selectByPrimaryKey(jobLog.getId());
        logger.info("trigger,jobLogId:{},jobId:{},triggerCode:{},handleCode:{},jobStatus:{}", jobLog.getId(), jobLog.getJobId(), triggerResult.getCode(), xxlJobLog.getHandleCode(), xxlJobLog.getJobStatus());

        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setId(jobInfo.getId());
        xxlJobInfo.setLastJobLogId(jobLog.getId());
        xxlJobInfo.setLastTriggerCode(triggerResult.getCode());
        xxlJobInfo.setLastTriggerTime(triggerTime);
        //调度成功
        xxlJobInfo.setJobStatus(xxlJobLog.getJobStatus());
        XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().updateByPrimaryKeySelective(xxlJobInfo);
        logger.debug(">>>>>>>>>>> xxl-job trigger end, jobId:{},jobRequestId:{}", jobInfo.getId(), jobRequestId);
    }

    /**
     * run executor
     *
     * @param triggerParam
     * @param address
     * @return
     */
    public static ReturnT<String> runExecutor(TriggerParam triggerParam, String address) {
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
            runResult = executorBiz.run(triggerParam);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> xxl-job trigger error, please check if the executor[{}] is running.", address, e);
            runResult = new ReturnT<String>(ReturnT.FAIL_CODE, ThrowableUtil.toString(e));
        }
        Map<String, Object> map = new LinkedHashMap<>(3);
        map.put("address", address);
        map.put("code", runResult.getCode());
        map.put("msg",  StrUtil.nullToEmpty(runResult.getMsg()));
        runResult.setMsg(JSONUtil.toJsonStr(map));
        return runResult;
    }

}
