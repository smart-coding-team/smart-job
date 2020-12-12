package com.smartcoding.schedule.mapper;

import com.smartcoding.schedule.core.model.XxlJobThread;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务线程Mapper接口
 *
 * @author wuque
 * @date 2020-08-29
 */
public interface XxlJobThreadMapper {
    /**
     * 查询任务线程
     *
     * @param id 任务线程ID
     * @return 任务线程
     */
    public XxlJobThread selectXxlJobThreadById(Long id);

    /**
     * 查询任务线程列表
     *
     * @param xxlJobThread 任务线程
     * @return 任务线程集合
     */
    public List<XxlJobThread> selectXxlJobThreadList(XxlJobThread xxlJobThread);

    /**
     * 新增任务线程
     *
     * @param xxlJobThread 任务线程
     * @return 结果
     */
    public int insertXxlJobThread(XxlJobThread xxlJobThread);

    /**
     * 修改任务线程
     *
     * @param xxlJobThread 任务线程
     * @return 结果
     */
    public int updateXxlJobThread(XxlJobThread xxlJobThread);

    /**
     * 删除任务线程
     *
     * @param id 任务线程ID
     * @return 结果
     */
    public int deleteXxlJobThreadById(Long id);

    /**
     * 批量删除任务线程
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteXxlJobThreadByIds(Long[] ids);

    public List<XxlJobThread> selectXxlJobThreadListByAddress(@Param("address") String address, @Param("codeList") List<Integer> codeList);

    XxlJobThread selectXxlJobThreadByAddress(@Param("threadType") Integer threadType, @Param("address") String address);

    int deleteXxlJobThreadByAddress(@Param("threadType")Integer threadType, @Param("address") String address,@Param("threadStatus")Integer threadStatus);

    int updateXxlJobThreadOnlineTime(@Param("threadType")Integer threadType, @Param("address") String address);



}
