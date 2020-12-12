

package com.smartcoding.schedule.mapper;

import com.smartcoding.schedule.core.model.XxlJobInfo;
import com.smartcoding.schedule.core.model.bo.XxlJobInfoBO;
import com.smartcoding.schedule.core.model.bo.XxlJobStatisticBO;
import com.smartcoding.schedule.core.model.bo.XxlShortJobInfoBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoMapper {

	int deleteByPrimaryKey(Long id);

	int insert(XxlJobInfo record);

	int insertSelective(XxlJobInfo record);

	XxlJobInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(XxlJobInfo record);

	int updateByPrimaryKeyWithBLOBs(XxlJobInfo record);

	int updateByPrimaryKey(XxlJobInfo record);


	List<XxlJobInfoBO> getJobList(
			                   @Param("id") Long id,
							  @Param("jobGroup") Long jobGroup,
							  @Param("glueType") String glueType,
							  @Param("jobStatus")  Integer jobStatus,
							  @Param("triggerStatus") Integer triggerStatus);

	int countJobByTriggerStatus(@Param("jobGroup") Long jobGroup, @Param("triggerStatus") int triggerStatus);

	 int update(XxlJobInfo xxlJobInfo);


	 int findAllCount();

	 List<XxlJobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime,  @Param("pagesize") int pagesize);

	 int scheduleUpdate(XxlJobInfo xxlJobInfo);


	XxlJobStatisticBO getXxlJobStatisticBO();

	List<XxlShortJobInfoBO> getXxlShortJobInfoBO(@Param("searchContent") String searchContent,@Param("pageSize")int pageSize);

	List<XxlShortJobInfoBO> getXxlShortJobInfoBOByNextExecute(@Param("startSecondsMilli") Long startSecondsMilli,@Param("endSecondsMilli") Long endSecondsMilli);

	XxlJobInfo getXxlJobInfo(@Param("jobGroup")Long jobGroup, @Param("executorHandler") String executorHandler);

	int updateJobStatus(XxlJobInfo record);

    List<XxlShortJobInfoBO> selectJobInfoByIdList(@Param("idList") List<Long> jobIdList);
}
