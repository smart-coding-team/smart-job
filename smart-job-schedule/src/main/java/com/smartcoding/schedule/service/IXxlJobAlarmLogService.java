package com.smartcoding.schedule.service;

import com.smartcoding.schedule.core.model.XxlJobAlarmLog;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 任务告警记录Service接口
 *
 * @author wuque
 * @date 2020-08-29
 */
public interface IXxlJobAlarmLogService {
    /**
     * 查询任务告警记录
     *
     * @param id 任务告警记录ID
     * @return 任务告警记录
     */
    XxlJobAlarmLog selectXxlJobAlarmLogById(Long id);

    /**
     * 查询任务告警记录列表
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 任务告警记录集合
     */
    List<XxlJobAlarmLog> selectXxlJobAlarmLogList(XxlJobAlarmLog xxlJobAlarmLog);

    /**
     * 新增任务告警记录
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 结果
     */
    int insertXxlJobAlarmLog(XxlJobAlarmLog xxlJobAlarmLog);

    /**
     * 修改任务告警记录
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 结果
     */
    int updateXxlJobAlarmLog(XxlJobAlarmLog xxlJobAlarmLog);

    /**
     * 批量删除任务告警记录
     *
     * @param ids 需要删除的任务告警记录ID
     * @return 结果
     */
    int deleteXxlJobAlarmLogByIds(Long[] ids);

    /**
     * 删除任务告警记录信息
     *
     * @param id 任务告警记录ID
     * @return 结果
     */
    int deleteXxlJobAlarmLogById(Long id);

    PageInfo<XxlJobAlarmLog> selectXxlJobAlarmLogListPage(int pageNum, int pageSize, String jobName, Boolean sendStatus, Long logId);
}
