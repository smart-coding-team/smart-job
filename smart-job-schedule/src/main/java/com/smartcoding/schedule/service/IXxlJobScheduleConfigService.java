package com.smartcoding.schedule.service;

import com.smartcoding.schedule.core.model.XxlJobScheduleConfig;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 任务管理配置Service接口
 *
 * @author wuque
 * @date 2020-08-29
 */
public interface IXxlJobScheduleConfigService {
    /**
     * 查询任务管理配置
     *
     * @param id 任务管理配置ID
     * @return 任务管理配置
     */
    public XxlJobScheduleConfig selectXxlJobScheduleConfigById(Long id);

    /**
     * 查询任务管理配置列表
     *
     * @param xxlJobScheduleConfig 任务管理配置
     * @return 任务管理配置集合
     */
    public List<XxlJobScheduleConfig> selectXxlJobScheduleConfigList(XxlJobScheduleConfig xxlJobScheduleConfig);

    PageInfo<XxlJobScheduleConfig> selectXxlJobScheduleConfigList(int pageNum, int pageSize, XxlJobScheduleConfig dictData);

    /**
     * 新增任务管理配置
     *
     * @param xxlJobScheduleConfig 任务管理配置
     * @return 结果
     */
    public int insertXxlJobScheduleConfig(XxlJobScheduleConfig xxlJobScheduleConfig);

    /**
     * 修改任务管理配置
     *
     * @param xxlJobScheduleConfig 任务管理配置
     * @return 结果
     */
    public int updateXxlJobScheduleConfig(XxlJobScheduleConfig xxlJobScheduleConfig);

    /**
     * 批量删除任务管理配置
     *
     * @param ids 需要删除的任务管理配置ID
     * @return 结果
     */
    public int deleteXxlJobScheduleConfigByIds(Long[] ids);

    /**
     * 删除任务管理配置信息
     *
     * @param id 任务管理配置ID
     * @return 结果
     */
    public int deleteXxlJobScheduleConfigById(Long id);


}
