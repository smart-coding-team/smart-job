package com.smartcoding.schedule.service;

import com.smartcoding.schedule.core.alarm.AlarmResult;
import com.smartcoding.schedule.core.model.XxlAlarmInfo;
import com.smartcoding.schedule.core.model.bo.XxlShortAlarmInfoBO;
import com.smartcoding.schedule.core.model.vo.TestAlarmParamVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 任务告警方式配置Service接口
 *
 * @author wuque
 * @date 2020-08-29
 */
public interface IXxlJobAlarmInfoService {
    /**
     * 查询任务告警方式配置
     *
     * @param id 任务告警方式配置ID
     * @return 任务告警方式配置
     */
    XxlAlarmInfo selectXxlJobAlarmInfoById(Long id);

    /**
     * 查询任务告警方式配置列表
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 任务告警方式配置集合
     */
    List<XxlAlarmInfo> selectXxlJobAlarmWayList(XxlAlarmInfo xxlAlarmInfo);

    PageInfo<XxlAlarmInfo> selectXxlJobAlarmWayList(int pageNum, int pageSize, XxlAlarmInfo dictData);

    /**
     * 新增任务告警方式配置
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 结果
     */
    int insertXxlJobAlarmWay(XxlAlarmInfo xxlAlarmInfo);

    /**
     * 修改任务告警方式配置
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 结果
     */
    int updateXxlJobAlarmWay(XxlAlarmInfo xxlAlarmInfo);

    /**
     * 删除任务告警方式配置信息
     *
     * @param id 任务告警方式配置ID
     * @return 结果
     */
    int deleteXxlJobAlarmWayById(Long id);

    XxlAlarmInfo selectXxlJobAlarmInfoByAlarmType(String alarmType);

    List<XxlShortAlarmInfoBO> getXxlShortAlarmInfoBO(String searchContent, int pageSize);

    List<XxlShortAlarmInfoBO> queryIds(List<Integer> idList);

    AlarmResult testSendAlarm(TestAlarmParamVO testAlarmParamVO);
}
