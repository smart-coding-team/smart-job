package cn.smartcoding.schedule.service.impl;

import cn.smartcoding.common.utils.DateUtils;
import cn.smartcoding.schedule.core.model.XxlJobScheduleConfig;
import cn.smartcoding.schedule.mapper.XxlJobScheduleConfigMapper;
import cn.smartcoding.schedule.service.IXxlJobScheduleConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务管理配置Service业务层处理
 *
 * @author wuque
 * @date 2020-08-29
 */
@Service
public class XxlJobScheduleConfigServiceImpl implements IXxlJobScheduleConfigService {
    @Autowired
    private XxlJobScheduleConfigMapper xxlJobScheduleConfigMapper;

    /**
     * 查询任务管理配置
     *
     * @param id 任务管理配置ID
     * @return 任务管理配置
     */
    @Override
    public XxlJobScheduleConfig selectXxlJobScheduleConfigById(Long id) {
        return xxlJobScheduleConfigMapper.selectXxlJobScheduleConfigById(id);
    }

    @Override
    public PageInfo<XxlJobScheduleConfig> selectXxlJobScheduleConfigList(int pageNum, int pageSize, XxlJobScheduleConfig dictData) {
        PageHelper.startPage(pageNum, pageSize);
        List<XxlJobScheduleConfig> list = selectXxlJobScheduleConfigList(dictData);
        return new PageInfo<>(list);
    }

    /**
     * 查询任务管理配置列表
     *
     * @param xxlJobScheduleConfig 任务管理配置
     * @return 任务管理配置
     */
    @Override
    public List<XxlJobScheduleConfig> selectXxlJobScheduleConfigList(XxlJobScheduleConfig xxlJobScheduleConfig) {
        return xxlJobScheduleConfigMapper.selectXxlJobScheduleConfigList(xxlJobScheduleConfig);
    }

    /**
     * 新增任务管理配置
     *
     * @param xxlJobScheduleConfig 任务管理配置
     * @return 结果
     */
    @Override
    public int insertXxlJobScheduleConfig(XxlJobScheduleConfig xxlJobScheduleConfig) {
        xxlJobScheduleConfig.setCreateTime(DateUtils.getNowDate());
        return xxlJobScheduleConfigMapper.insertXxlJobScheduleConfig(xxlJobScheduleConfig);
    }

    /**
     * 修改任务管理配置
     *
     * @param xxlJobScheduleConfig 任务管理配置
     * @return 结果
     */
    @Override
    public int updateXxlJobScheduleConfig(XxlJobScheduleConfig xxlJobScheduleConfig) {
        xxlJobScheduleConfig.setUpdateTime(DateUtils.getNowDate());
        return xxlJobScheduleConfigMapper.updateXxlJobScheduleConfig(xxlJobScheduleConfig);
    }

    /**
     * 批量删除任务管理配置
     *
     * @param ids 需要删除的任务管理配置ID
     * @return 结果
     */
    @Override
    public int deleteXxlJobScheduleConfigByIds(Long[] ids) {
        return xxlJobScheduleConfigMapper.deleteXxlJobScheduleConfigByIds(ids);
    }

    /**
     * 删除任务管理配置信息
     *
     * @param id 任务管理配置ID
     * @return 结果
     */
    @Override
    public int deleteXxlJobScheduleConfigById(Long id) {
        return xxlJobScheduleConfigMapper.deleteXxlJobScheduleConfigById(id);
    }
}
