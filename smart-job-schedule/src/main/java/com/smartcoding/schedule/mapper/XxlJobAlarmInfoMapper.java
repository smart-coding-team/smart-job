package com.smartcoding.schedule.mapper;

import com.smartcoding.schedule.core.model.XxlAlarmInfo;
import com.smartcoding.schedule.core.model.bo.XxlShortAlarmInfoBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务告警方式配置Mapper接口
 *
 * @author wuque
 * @date 2020-08-29
 */
public interface XxlJobAlarmInfoMapper {
    /**
     * 查询任务告警方式配置
     *
     * @param id 任务告警方式配置ID
     * @return 任务告警方式配置
     */
    public XxlAlarmInfo selectXxlJobAlarmWayById(Long id);

    /**
     * 查询任务告警方式配置列表
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 任务告警方式配置集合
     */
    public List<XxlAlarmInfo> selectXxlJobAlarmWayList(XxlAlarmInfo xxlAlarmInfo);

    /**
     * 新增任务告警方式配置
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 结果
     */
    public int insertXxlJobAlarmWay(XxlAlarmInfo xxlAlarmInfo);

    /**
     * 修改任务告警方式配置
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 结果
     */
    public int updateXxlJobAlarmWay(XxlAlarmInfo xxlAlarmInfo);

    /**
     * 删除任务告警方式配置
     *
     * @param id 任务告警方式配置ID
     * @return 结果
     */
    public int deleteXxlJobAlarmWayById(Long id);

    /**
     * 批量删除任务告警方式配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteXxlJobAlarmWayByIds(Long[] ids);

    XxlAlarmInfo selectXxlJobAlarmInfoByAlarmType(String alarmType);

    List<XxlShortAlarmInfoBO> querySearch(@Param("searchContent") String searchContent,@Param("pageSize") int pageSize);

    List<XxlShortAlarmInfoBO> queryIds(@Param("idList") List<Integer> idList);
}
