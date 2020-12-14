package cn.smartcoding.schedule.core.trigger;

public interface TriggerConstant {
    String TRIGGER_TYPE = "jobconf_trigger_type";
    String TRIGGER_ADMIN_ADDRESS = "jobconf_trigger_admin_adress";
    String TRIGGER_EXE_REG_TYPE = "jobconf_trigger_exe_regtype";
    String TRIGGER_EXE_REG_ADDRESS = "jobconf_trigger_exe_regaddress";
    String EXECUTOR_ROUTE_STRATEGY = "jobinfo_field_executorRouteStrategy";
    String EXECUTOR_BLOCK_STRATEGY = "jobinfo_field_executorBlockStrategy";
    String EXECUTOR_FAIL_RETRY_COUNT = "jobinfo_field_executorFailRetryCount";
    String TIMEOUT = "jobinfo_field_timeout";
    //触发调度
    String TRIGGER_RUN = "jobconf_trigger_run";
    //心跳检测
    String TRIGGER_BEAT = "jobconf_beat";
    String SHARDING_PARAM = "shardingParam";

}
