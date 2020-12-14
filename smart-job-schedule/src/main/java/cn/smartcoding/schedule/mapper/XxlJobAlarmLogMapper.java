package cn.smartcoding.schedule.mapper;

import cn.smartcoding.schedule.core.model.XxlJobAlarmLog;

import java.util.List;

/**
 * 任务告警记录Mapper接口
 *
 * @author wuque
 * @date 2020-08-29
 */
public interface XxlJobAlarmLogMapper {
    /**
     * 查询任务告警记录
     *
     * @param id 任务告警记录ID
     * @return 任务告警记录
     */
    public XxlJobAlarmLog selectXxlJobAlarmLogById(Long id);

    /**
     * 查询任务告警记录列表
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 任务告警记录集合
     */
    public List<XxlJobAlarmLog> selectXxlJobAlarmLogList(XxlJobAlarmLog xxlJobAlarmLog);

    /**
     * 新增任务告警记录
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 结果
     */
    public int insertXxlJobAlarmLog(XxlJobAlarmLog xxlJobAlarmLog);

    /**
     * 修改任务告警记录
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 结果
     */
    public int updateXxlJobAlarmLog(XxlJobAlarmLog xxlJobAlarmLog);

    /**
     * 删除任务告警记录
     *
     * @param id 任务告警记录ID
     * @return 结果
     */
    public int deleteXxlJobAlarmLogById(Long id);

    /**
     * 批量删除任务告警记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteXxlJobAlarmLogByIds(Long[] ids);
}
