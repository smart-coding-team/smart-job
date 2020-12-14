

package cn.smartcoding.schedule.mapper;

import cn.smartcoding.schedule.core.model.XxlJobLogGlue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * job log for glue
 * @author xuxueli 2016-5-19 18:04:56
 */
@Mapper
public interface XxlJobLogGlueMapper {

	 int save(XxlJobLogGlue xxlJobLogGlue);

	 List<XxlJobLogGlue> findByJobId(@Param("jobId") Long jobId);

	 int removeOld(@Param("jobId") Long jobId, @Param("limit") int limit);

	 int deleteByJobId(@Param("jobId") Long jobId);

}
