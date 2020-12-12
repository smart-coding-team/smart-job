package com.smartcoding.schedule.service.impl;

import com.smartcoding.schedule.core.model.XxlJobAlarmLog;
import com.smartcoding.schedule.mapper.XxlJobAlarmLogMapper;
import com.smartcoding.schedule.service.IXxlJobAlarmLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务告警记录Service业务层处理
 *
 * @author wuque
 * @date 2020-08-29
 */
@Service
public class XxlJobAlarmLogServiceImpl implements IXxlJobAlarmLogService {
    @Resource
    private XxlJobAlarmLogMapper xxlJobAlarmLogMapper;

    /**
     * 查询任务告警记录
     *
     * @param id 任务告警记录ID
     * @return 任务告警记录
     */
    @Override
    public XxlJobAlarmLog selectXxlJobAlarmLogById(Long id) {
        return xxlJobAlarmLogMapper.selectXxlJobAlarmLogById(id);
    }

    /**
     * 查询任务告警记录列表
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 任务告警记录
     */
    @Override
    public List<XxlJobAlarmLog> selectXxlJobAlarmLogList(XxlJobAlarmLog xxlJobAlarmLog) {
        return xxlJobAlarmLogMapper.selectXxlJobAlarmLogList(xxlJobAlarmLog);
    }

    /**
     * 新增任务告警记录
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 结果
     */
    @Override
    public int insertXxlJobAlarmLog(XxlJobAlarmLog xxlJobAlarmLog) {
        return xxlJobAlarmLogMapper.insertXxlJobAlarmLog(xxlJobAlarmLog);
    }

    /**
     * 修改任务告警记录
     *
     * @param xxlJobAlarmLog 任务告警记录
     * @return 结果
     */
    @Override
    public int updateXxlJobAlarmLog(XxlJobAlarmLog xxlJobAlarmLog) {
        return xxlJobAlarmLogMapper.updateXxlJobAlarmLog(xxlJobAlarmLog);
    }

    /**
     * 批量删除任务告警记录
     *
     * @param ids 需要删除的任务告警记录ID
     * @return 结果
     */
    @Override
    public int deleteXxlJobAlarmLogByIds(Long[] ids) {
        return xxlJobAlarmLogMapper.deleteXxlJobAlarmLogByIds(ids);
    }

    /**
     * 删除任务告警记录信息
     *
     * @param id 任务告警记录ID
     * @return 结果
     */
    @Override
    public int deleteXxlJobAlarmLogById(Long id) {
        return xxlJobAlarmLogMapper.deleteXxlJobAlarmLogById(id);
    }

    @Override
    public PageInfo<XxlJobAlarmLog> selectXxlJobAlarmLogListPage(int pageNum, int pageSize, String jobName, Boolean sendStatus, Long logId) {
        PageHelper.startPage(pageNum, pageSize);
        XxlJobAlarmLog xxlJobAlarmLog=new XxlJobAlarmLog();
        xxlJobAlarmLog.setJobName(jobName);
        xxlJobAlarmLog.setLogId(logId);
        xxlJobAlarmLog.setSendStatus(sendStatus);
        List<XxlJobAlarmLog> xxlJobAlarmLogs = xxlJobAlarmLogMapper.selectXxlJobAlarmLogList(xxlJobAlarmLog);
        return new PageInfo<XxlJobAlarmLog>(xxlJobAlarmLogs);
    }
}
