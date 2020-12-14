

package cn.smartcoding.schedule.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.smartcoding.common.core.domain.CommonErrorCode;
import cn.smartcoding.common.exception.CommonException;
import cn.smartcoding.job.core.enums.RegistryConfig;
import cn.smartcoding.schedule.core.enums.AddressOnlineEnum;
import cn.smartcoding.schedule.core.enums.AddressTypeEnum;
import cn.smartcoding.schedule.core.model.XxlJobGroup;
import cn.smartcoding.schedule.core.model.XxlJobRegistry;
import cn.smartcoding.schedule.core.model.vo.JobAddressGroupVO;
import cn.smartcoding.schedule.core.model.vo.JobAddressVO;
import cn.smartcoding.schedule.core.model.vo.JobGroupVO;
import cn.smartcoding.schedule.core.trigger.TriggerStatusEnum;
import cn.smartcoding.schedule.core.util.I18nUtil;
import cn.smartcoding.schedule.core.util.StringBuilderUtil;
import cn.smartcoding.schedule.mapper.XxlJobGroupMapper;
import cn.smartcoding.schedule.mapper.XxlJobInfoMapper;
import cn.smartcoding.schedule.mapper.XxlJobRegistryMapper;
import cn.smartcoding.schedule.service.XxlJobGroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 无缺
 * @date 2019-07-13
 */
@Service
public class XxlJobGroupServiceImpl implements XxlJobGroupService {

    @Resource
    private XxlJobRegistryMapper xxlJobRegistryMapper;

    @Resource
    private XxlJobGroupMapper jobGroupMapper;
    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;

    @Override
    public JobAddressGroupVO getXxlJobAddressBOS(List<String> registryList, String appName) {
        JobAddressGroupVO jobAddressGroupVO = new JobAddressGroupVO();
        List<JobAddressVO> xxlJobAddressBOList = new ArrayList<>();
        Map<String, XxlJobRegistry> map = getStringXxlJobRegistryMap(registryList, appName);
        registryList.forEach(address -> {
            XxlJobRegistry xxlJobRegistry = map.get(address);
            JobAddressVO xxlJobAddressBO = new JobAddressVO(address, AddressOnlineEnum.OFFLINE.getCode(), null);
            if (xxlJobRegistry != null) {
                xxlJobAddressBO.setOnline(AddressOnlineEnum.ONLINE.getCode());
                xxlJobAddressBO.setUpdateTime(xxlJobRegistry.getUpdateTime());
                jobAddressGroupVO.addOnLineNum();
            }
            xxlJobAddressBOList.add(xxlJobAddressBO);
        });
        jobAddressGroupVO.setAppName(appName);
        jobAddressGroupVO.setRegistryInfoList(xxlJobAddressBOList);
        return jobAddressGroupVO;
    }

    @Override
    public int getOnLineNum(List<String> registryList, String appName) {
        AtomicInteger onlineNum = new AtomicInteger();
        Map<String, XxlJobRegistry> map = getStringXxlJobRegistryMap(registryList, appName);
        registryList.forEach(address -> {
            XxlJobRegistry xxlJobRegistry = map.get(address);
            if (xxlJobRegistry != null) {
                onlineNum.addAndGet(1);
            }
        });
        return onlineNum.get();
    }

    private Map<String, XxlJobRegistry> getStringXxlJobRegistryMap(List<String> registryList, String appName) {
        List<XxlJobRegistry> xxlJobRegistryList = CollectionUtil.isNotEmpty(registryList) ? xxlJobRegistryMapper.getXxlJobRegistryList(RegistryConfig.RegistType.EXECUTOR.name(), appName, registryList) : new ArrayList<>();
        return xxlJobRegistryList.stream().collect(Collectors.toMap(XxlJobRegistry::getRegistryValue, xxlJobRegistry -> xxlJobRegistry));
    }


    @Override
    public PageInfo<JobGroupVO> pageList(int pageNum, int pageSize, Long id, Integer addressType, Integer alarmStatus) {
        PageHelper.startPage(pageNum, pageSize);
        // page list
        List<JobGroupVO> list = jobGroupMapper.getList(id,addressType,alarmStatus);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(xxlJobGroupBO -> {
                List<String> registryList = xxlJobGroupBO.getRegistryList();
                if (CollectionUtil.isNotEmpty(registryList)) {
                    JobAddressGroupVO xxlJobAddressBOS = getXxlJobAddressBOS(registryList, xxlJobGroupBO.getAppName());
                    xxlJobGroupBO.setRegistryInfoList(xxlJobAddressBOS.getRegistryInfoList());
                    xxlJobGroupBO.setOnLineNum(xxlJobAddressBOS.getOnLineNum());
                }
            });
        }

        return new PageInfo<JobGroupVO>(list);
    }

    @Override
    public int addJobGroup(XxlJobGroup jobGroup) {
        String appName = StrUtil.trimToNull(jobGroup.getAppName());
        String alarmIds = StrUtil.trimToNull(jobGroup.getAlarmIds());
        String title = StrUtil.trimToNull(jobGroup.getTitle());
        Integer addressType = jobGroup.getAddressType();
        Integer order = jobGroup.getOrder();
        Integer alarmStatus = jobGroup.getAlarmStatus();
        String addressList = StrUtil.trimToNull(jobGroup.getAddressList());
        XxlJobGroup xxlJobGroup = new XxlJobGroup();
        // valid
        if (jobGroup.getAppName() == null || jobGroup.getAppName().trim().length() == 0) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (jobGroup.getAppName().length() < 4 || jobGroup.getAppName().length() > 64) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobgroup_field_appName_length"));
        }
        if (jobGroup.getTitle() == null || jobGroup.getTitle().trim().length() == 0) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        XxlJobGroup existXxlJobGroup = jobGroupMapper.selectByAppName(appName);
        if (existXxlJobGroup != null) {
            throw new CommonException(CommonErrorCode.ERROR, "appName已存在");
        }
        if (AddressTypeEnum.ADD.getCode().equals(addressType)) {
            if (StrUtil.isEmpty(addressList)) {
                throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            List<String> itemList = StrUtil.split(addressList, ',', true, true);
            if (CollectionUtil.isEmpty(itemList)) {
                throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
            }
            xxlJobGroup.setAddressList(StringBuilderUtil.listToString(itemList));
        }
        if (alarmStatus!=null&&alarmStatus==1&&StrUtil.isEmpty(alarmIds) ) {
            throw new CommonException(CommonErrorCode.ERROR, "开启告警，至少选择一种告警方式");
        }
        xxlJobGroup.setAppName(appName);
        xxlJobGroup.setAddressType(addressType);
        xxlJobGroup.setTitle(title);
        xxlJobGroup.setOrder(order);
        xxlJobGroup.setAlarmStatus(alarmStatus);
        xxlJobGroup.setAlarmIds(alarmIds);

        return jobGroupMapper.insertSelective(jobGroup);
    }

    @Override
    public int update(XxlJobGroup jobGroupVO) {
        String appName = StrUtil.trimToNull(jobGroupVO.getAppName());
        String alarmIds = StrUtil.trimToNull(jobGroupVO.getAlarmIds());
        String title = StrUtil.trimToNull(jobGroupVO.getTitle());
        Integer addressType = jobGroupVO.getAddressType();
        Integer order = jobGroupVO.getOrder();
        Integer alarmStatus = jobGroupVO.getAlarmStatus();
        String addressList = StrUtil.trimToNull(jobGroupVO.getAddressList());
        // valid
        if (StrUtil.isEmpty(appName)) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (appName.length() < 4 || appName.length() > 64) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobgroup_field_appName_length"));
        }
        if (title == null) {
            throw new CommonException(CommonErrorCode.ERROR, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        XxlJobGroup existXxlJobGroup = jobGroupMapper.selectByAppName(appName);
        if (existXxlJobGroup != null && !existXxlJobGroup.getId().equals(jobGroupVO.getId())) {
            throw new CommonException(CommonErrorCode.ERROR, "appName已存在");
        }
        XxlJobGroup oldXxlJobGroup = jobGroupMapper.selectByPrimaryKey(jobGroupVO.getId());
        if (oldXxlJobGroup == null) {
            throw new CommonException(CommonErrorCode.ERROR, "不存在");
        }
        //开启
        if (alarmStatus != null && alarmStatus == 1 && StrUtil.isEmpty(alarmIds)) {
            throw new CommonException(CommonErrorCode.ERROR, "开启告警，至少选择一种告警方式");
        }
        XxlJobGroup xxlJobGroup = new XxlJobGroup();
        xxlJobGroup.setId(jobGroupVO.getId());
        xxlJobGroup.setAppName(appName);
        xxlJobGroup.setAddressType(addressType);
        xxlJobGroup.setTitle(title);
        xxlJobGroup.setOrder(order);
        xxlJobGroup.setClientVersion(jobGroupVO.getClientVersion());
        xxlJobGroup.setAlarmStatus(alarmStatus);
        xxlJobGroup.setAlarmIds(alarmIds);
        //手动录入
        if (AddressTypeEnum.ADD.getCode().equals(addressType)) {
            if (StrUtil.isEmpty(addressList)) {
                throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            List<String> itemList = StrUtil.split(addressList, ',', true, true);
            if (CollectionUtil.isEmpty(itemList)) {
                throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
            }
            xxlJobGroup.setAddressList(StringBuilderUtil.listToString(itemList));
        } else if (AddressTypeEnum.ADD.getCode().equals(oldXxlJobGroup.getAddressType()) && AddressTypeEnum.AUTO.getCode().equals(addressType)) {
            xxlJobGroup.setAddressList("");
        }
        int ret = jobGroupMapper.updateByPrimaryKeySelective(xxlJobGroup);
        return ret;
    }

    @Override
    public int remove(Long id) {
        // valid
        int count = xxlJobInfoMapper.countJobByTriggerStatus(id, TriggerStatusEnum.READY.getCode());
        if (count > 0) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobgroup_del_limit_0"));
        }
        List<XxlJobGroup> allList = jobGroupMapper.findAll();
        if (allList.size() == 1) {
            throw new CommonException(CommonErrorCode.ERROR, I18nUtil.getString("jobgroup_del_limit_1"));
        }
        int ret = jobGroupMapper.deleteByPrimaryKey(id);
        return ret;
    }

    @Override
    public XxlJobGroup loadById(Long id) {
        XxlJobGroup jobGroup = jobGroupMapper.selectByPrimaryKey(id);
        return jobGroup;
    }

    @Override
    public JobAddressGroupVO getOnLineAddressList(Long id) {
        XxlJobGroup jobGroup = jobGroupMapper.selectByPrimaryKey(id);
        List<String> registryList = StrUtil.split(jobGroup.getAddressList(), ',', true, true);
        if (CollectionUtil.isNotEmpty(registryList)) {
            return getXxlJobAddressBOS(registryList, jobGroup.getAppName());
        }
        return null;
    }
    @Override
    public List<XxlJobGroup> findAll() {
        return jobGroupMapper.findAll();
    }

    @Override
    public List<XxlJobGroup> querySearch(String title, int pageSize) {
        return jobGroupMapper.querySearch(title,pageSize);
    }
}
