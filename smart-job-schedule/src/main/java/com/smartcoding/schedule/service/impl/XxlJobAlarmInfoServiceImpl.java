package com.smartcoding.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.smartcoding.common.core.domain.entity.SysUser;
import com.smartcoding.common.utils.DateUtils;
import com.smartcoding.schedule.core.alarm.*;
import com.smartcoding.schedule.core.alarm.*;
import com.smartcoding.schedule.core.model.XxlAlarmInfo;
import com.smartcoding.schedule.core.model.bo.XxlShortAlarmInfoBO;
import com.smartcoding.schedule.core.model.vo.NoticeUser;
import com.smartcoding.schedule.core.model.vo.TestAlarmParamVO;
import com.smartcoding.schedule.core.model.vo.Variable;
import com.smartcoding.schedule.mapper.XxlJobAlarmInfoMapper;
import com.smartcoding.schedule.service.IXxlJobAlarmInfoService;
import com.smartcoding.system.service.ISysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 任务告警方式配置Service业务层处理
 *
 * @author wuque
 * @date 2020-08-29
 */
@Service
@CacheConfig(cacheNames = "jobAlarm")
public class XxlJobAlarmInfoServiceImpl implements IXxlJobAlarmInfoService {
    @Resource
    private XxlJobAlarmInfoMapper xxlJobAlarmInfoMapper;
    @Resource
    private Collection<JobAlarm> jobAlarms;
    @Resource
    private ISysUserService sysUserService;

    /**
     * 查询任务告警方式配置
     *
     * @param id 任务告警方式配置ID
     * @return 任务告警方式配置
     */
    @Override
    @Cacheable(key = "'id:' + #p0")
    public XxlAlarmInfo selectXxlJobAlarmInfoById(Long id) {
        return xxlJobAlarmInfoMapper.selectXxlJobAlarmWayById(id);
    }

    /**
     * 查询任务告警方式配置列表
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 任务告警方式配置
     */
    @Override
    public List<XxlAlarmInfo> selectXxlJobAlarmWayList(XxlAlarmInfo xxlAlarmInfo) {
        return xxlJobAlarmInfoMapper.selectXxlJobAlarmWayList(xxlAlarmInfo);
    }

    @Override
    public PageInfo<XxlAlarmInfo> selectXxlJobAlarmWayList(int pageNum, int pageSize, XxlAlarmInfo dictData) {
        PageHelper.startPage(pageNum, pageSize);
        List<XxlAlarmInfo> list = selectXxlJobAlarmWayList(dictData);
        return new PageInfo<>(list);
    }

    /**
     * 新增任务告警方式配置
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 结果
     */
    @Override
    public int insertXxlJobAlarmWay(XxlAlarmInfo xxlAlarmInfo) {
        xxlAlarmInfo.setCreateTime(DateUtils.getNowDate());
        return xxlJobAlarmInfoMapper.insertXxlJobAlarmWay(xxlAlarmInfo);
    }

    /**
     * 修改任务告警方式配置
     *
     * @param xxlAlarmInfo 任务告警方式配置
     * @return 结果
     */
    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    public int updateXxlJobAlarmWay(XxlAlarmInfo xxlAlarmInfo) {
        xxlAlarmInfo.setUpdateTime(DateUtils.getNowDate());
        return xxlJobAlarmInfoMapper.updateXxlJobAlarmWay(xxlAlarmInfo);
    }

    /**
     * 删除任务告警方式配置信息
     *
     * @param id 任务告警方式配置ID
     * @return 结果
     */
    @Override
    @CacheEvict(key = "'id:' + #p0")
    public int deleteXxlJobAlarmWayById(Long id) {
        return xxlJobAlarmInfoMapper.deleteXxlJobAlarmWayById(id);
    }

    @Override
    public XxlAlarmInfo selectXxlJobAlarmInfoByAlarmType(String alarmType) {
        return xxlJobAlarmInfoMapper.selectXxlJobAlarmInfoByAlarmType(alarmType);
    }

    @Override
    public List<XxlShortAlarmInfoBO> getXxlShortAlarmInfoBO(String searchContent, int pageSize) {
        return xxlJobAlarmInfoMapper.querySearch(searchContent, pageSize);
    }

    @Override
    public List<XxlShortAlarmInfoBO> queryIds(List<Integer> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            return new ArrayList<>();
        }
        return xxlJobAlarmInfoMapper.queryIds(idList);
    }

    @Override
    public AlarmResult testSendAlarm(TestAlarmParamVO testAlarmParamVO) {
        XxlAlarmInfo alarmInfo = testAlarmParamVO.getAlarmInfo();
        Assert.notNull(alarmInfo, "alarmInfo  cannot be null");
        String alarmType = alarmInfo.getAlarmType();
        JobAlarmEnum jobAlarmEnum = JobAlarmEnum.match(alarmType, null);
        List<Variable> variableList = testAlarmParamVO.getVariableList();
        Map<String, String> map = new HashMap<String,String>();
        variableList.forEach( variable -> {
            map.put(variable.getProp(), variable.getValue());
        });
        AlarmParam alarmParam = BeanUtil.copyProperties(map, AlarmParam.class);
        alarmParam.setAlarmInfo(alarmInfo);
        alarmParam.setJobAlarmEnum(jobAlarmEnum);
        alarmParam.setNoticeUserList(getSysUsers(testAlarmParamVO.getNoticeUser()));
        AlarmManager alarmProviderManager = new AlarmProviderManager(jobAlarms);
        return alarmProviderManager.testSendAlarm(alarmParam);
    }

    private List<SysUser> getSysUsers(NoticeUser noticeUser) {
        List<SysUser> userList = new ArrayList<>();
        if (noticeUser != null) {
            SysUser sysUser = new SysUser();
            sysUser.setMobile(noticeUser.getMobile());
            sysUser.setEmail(noticeUser.getEmail());
            userList.add(sysUser);
        }
        return userList;
    }


}
