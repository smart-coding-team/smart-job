

package com.smartcoding.schedule.mapper;

import com.smartcoding.schedule.core.model.XxlJobRegistry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobRegistryMapper {

     int deleteByPrimaryKey(Long id);

     int insert(XxlJobRegistry record);

     int insertSelective(XxlJobRegistry record);

     XxlJobRegistry selectByPrimaryKey(Long id);

     int updateByPrimaryKeySelective(XxlJobRegistry record);

     int updateByPrimaryKey(XxlJobRegistry record);

      List<Integer> findDead(@Param("timeout") int timeout);

      int removeDead(@Param("ids") List<Integer> ids);

     List<XxlJobRegistry> findAll(@Param("timeout") int timeout);

     List<XxlJobRegistry> findByRegistryGroup(@Param("timeout")int deadTimeout, @Param("registryGroup")String registryGroup);

     int registryUpdate(@Param("registryGroup") String registryGroup,
                              @Param("registryKey") String registryKey,
                              @Param("registryValue") String registryValue);

     int registrySave(@Param("registryGroup") String registryGroup,
                            @Param("registryKey") String registryKey,
                            @Param("registryValue") String registryValue);

     int registryDelete(@Param("registryGroup") String registryGroup,
                          @Param("registryKey") String registryKey,
                          @Param("registryValue") String registryValue);

     List<String> getOnLineAddressList(@Param("registryGroup") String registryGroup,
                      @Param("registryKey") String registryKey,
                      @Param("registryValueList") List<String> registryValueList);

     List<XxlJobRegistry> getXxlJobRegistryList(@Param("registryGroup") String registryGroup,
                                       @Param("registryKey") String registryKey,
                                       @Param("registryValueList") List<String> registryValueList);


}
