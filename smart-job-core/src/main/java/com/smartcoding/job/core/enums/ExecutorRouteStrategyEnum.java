package com.smartcoding.job.core.enums;

/**
 * 任务执行的路由策略
 * Created by 无缺 on 17/3/10.
 */
public enum ExecutorRouteStrategyEnum {

    /**
     * 故障转移
     */
    FAILOVER("jobconf_route_failover"),
    /**
     * 第一个
     */
    FIRST("jobconf_route_first"),
    /**
     * 最后一个
     */
    LAST("jobconf_route_last"),
    /**
     * 轮询
     */
    ROUND("jobconf_route_round"),
    /**
     * 随机
     */
    RANDOM("jobconf_route_random"),
    /**
     * 一致性HASH
     */
    CONSISTENT_HASH("jobconf_route_consistenthash"),
    /**
     * 最不经常使用
     */
    LEAST_FREQUENTLY_USED("jobconf_route_lfu"),
    /**
     * 最近最久未使用
     */
    LEAST_RECENTLY_USED("jobconf_route_lru"),
    /**
     * 忙碌转移
     */
    BUSYOVER("jobconf_route_busyover"),
    /**
     * 分片广播
     */
    SHARDING_BROADCAST("jobconf_route_shard");

    ExecutorRouteStrategyEnum(String title) {
        this.title = title;
    }

    private String title;

    public String getTitle() {
        return title;
    }


    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem) {
        if (name == null) {
            return defaultItem;
        }
        for (ExecutorRouteStrategyEnum item : ExecutorRouteStrategyEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }

}
