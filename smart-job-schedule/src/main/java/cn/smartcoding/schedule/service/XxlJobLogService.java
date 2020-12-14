package cn.smartcoding.schedule.service;

import cn.smartcoding.schedule.core.model.XxlJobLog;
import com.github.pagehelper.PageInfo;

import java.util.Date;

public interface XxlJobLogService {


    PageInfo<XxlJobLog> pageList(int pageNum, int pageSize,Long id, Integer jobStatus, Long jobId, Long jobGroup, Date triggerTimeStart, Date triggerTimeEnd);

    XxlJobLog selectByPrimaryKey(Long logId);

    void killRunningJob(Long id);

    void clearLog(Long jobGroup, Long jobId, Integer type, Integer num);
}
