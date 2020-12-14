package cn.smartcoding.schedule.service;

import cn.smartcoding.schedule.core.enums.ThreadTypeEnum;
import cn.smartcoding.schedule.core.model.XxlJobThread;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 任务调度Service接口
 *
 * @author wuque
 * @date 2020-08-29
 */
public interface IXxlJobTreadService {

    boolean isOpenThread(ThreadTypeEnum threadTypeEnum);

    boolean autoDeleteOpenThread(ThreadTypeEnum threadTypeEnum);

    /**
     * 查询任务调度
     *
     * @param id 任务调度ID
     * @return 任务调度
     */
    XxlJobThread selectXxlJobThreadById(Long id);

    /**
     * 查询任务调度列表
     *
     * @param xxlJobThread 任务调度
     * @return 任务调度集合
     */
    List<XxlJobThread> selectXxlJobThreadList(XxlJobThread xxlJobThread);

    PageInfo<XxlJobThread> selectXxlJobThreadList(int pageNum, int pageSize, XxlJobThread dictData);

    /**
     * 新增任务调度
     *
     * @param xxlJobThread 任务调度
     * @return 结果
     */
    int insertXxlJobThread(XxlJobThread xxlJobThread);

    /**
     * 修改任务调度
     *
     * @param xxlJobThread 任务调度
     * @return 结果
     */
    int updateXxlJobThread(XxlJobThread xxlJobThread);

    /**
     * 批量删除任务调度
     *
     * @param ids 需要删除的任务调度ID
     * @return 结果
     */
    int deleteXxlJobThreadByIds(Long[] ids);

    /**
     * 删除任务调度信息
     *
     * @param id 任务调度ID
     * @return 结果
     */
    int deleteXxlJobScheduleById(Long id);

    boolean stop(Long id, String userName);

    boolean start(Long id, String userName);

    void changeStatus(Long id, Integer status, String username);


}
