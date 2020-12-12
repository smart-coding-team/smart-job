

package com.smartcoding.schedule.mapper;

import com.smartcoding.schedule.core.model.XxlJobGroup;
import com.smartcoding.schedule.core.model.bo.XxlJobGroupBO;
import com.smartcoding.schedule.core.model.vo.JobGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobGroupMapper {

     int deleteByPrimaryKey(Long id);

     int insert(XxlJobGroup record);

     int insertSelective(XxlJobGroup record);

     XxlJobGroup selectByPrimaryKey(Long id);

     int updateByPrimaryKeySelective(XxlJobGroup record);

     int updateByPrimaryKey(XxlJobGroup record);


     List<XxlJobGroup> findAll();

     List<XxlJobGroupBO> findAllBO();

     List<XxlJobGroup> findByAddressType(@Param("addressType") int addressType);

     int update(XxlJobGroup xxlJobGroup);

    List<XxlJobGroupBO> pageList(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("appName")String appName);

    List<JobGroupVO> getList(@Param("id") Long id, @Param("addressType")Integer addressType, @Param("alarmStatus")Integer alarmStatus);

    int pageListCount(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("appName")String appName);

     XxlJobGroup selectByAppName(@Param("appName")String appName);

    List<XxlJobGroup> querySearch(@Param("title")String title, @Param("pageSize")int pageSize);

    int count();

}
