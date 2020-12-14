

package cn.smartcoding.schedule.mapper;

import cn.smartcoding.schedule.core.model.XxlJobLog;
import cn.smartcoding.schedule.core.model.bo.XxlJobLogReportBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * job log
 *
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(XxlJobLog record);

    int insertSelective(XxlJobLog record);

    XxlJobLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(XxlJobLog record);

    int updateByPrimaryKey(XxlJobLog record);

    List<XxlJobLog> getJobLogList(@Param("id") Long id, @Param("jobStatus") Integer jobStatus, @Param("jobId") Long jobId, @Param("jobGroup") Long jobGroup,
                                  @Param("triggerTimeStart") Date triggerTimeStart,
                                  @Param("triggerTimeEnd") Date triggerTimeEnd);

    int delete(@Param("jobId") Long jobId);


    XxlJobLogReportBO triggerCountByDay(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    int clearLogBeforeTime(@Param("jobGroup") Long jobGroup, @Param("jobId") Long jobId, @Param("clearBeforeTime") Date clearBeforeTime);

    int clearLogByBeforeNum(@Param("jobGroup") Long jobGroup, @Param("jobId") Long jobId, @Param("clearBeforeNum") int clearBeforeNum);

    int clearLogAll(@Param("jobGroup") Long jobGroup, @Param("jobId") Long jobId);

    List<Long> findTriggerFailJobLogIds(@Param("pagesize") int pagesize);

    List<Long> findHandleFailJobLogIds(@Param("pagesize") int pagesize);

    int updateAlarmStatus(@Param("logId") Long logId, @Param("oldAlarmStatus") int oldAlarmStatus, @Param("newAlarmStatus") int newAlarmStatus);

    int countXxlJobLog(@Param("now") LocalDate now);

    List<Long> getXxlJobLogListByGmtCreate(@Param("now") LocalDate now, @Param("pageSize") int pageSize);

    int updateJobStatusByHandleCode(@Param("id")Long id, @Param("handleCode") int handleCode);

    int updateJobStatusByTriggerCode(@Param("id")Long id, @Param("triggerCode") int triggerCode);
}
