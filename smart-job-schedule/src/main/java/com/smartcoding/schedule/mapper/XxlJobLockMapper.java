

package com.smartcoding.schedule.mapper;

import com.smartcoding.schedule.core.model.XxlJobLock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface XxlJobLockMapper {
    int deleteByPrimaryKey(String lockName);

    int insert(XxlJobLock record);

    int insertSelective(XxlJobLock record);

    XxlJobLock selectByPrimaryKey(String lockName);

    int updateByPrimaryKeySelective(XxlJobLock record);

    int updateByPrimaryKey(XxlJobLock record);

    int insertLockName(@Param("lockName") String lockName);
}
