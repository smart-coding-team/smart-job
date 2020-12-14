package cn.smartcoding.schedule.core.model.vo;

import cn.hutool.core.util.StrUtil;
import cn.smartcoding.schedule.core.model.XxlJobGroup;

import java.util.List;

public class JobGroupVO extends XxlJobGroup {
    //执行器地址列表(系统注册)
    private List<String> registryList;

    public List<String> getRegistryList() {
        String addressList = super.getAddressList();
        registryList = StrUtil.split(addressList, ',', true, true);
        return registryList;
    }

    private List<JobAddressVO> registryInfoList;
    private int onLineNum;

    public void addOnLineNum() {
        this.onLineNum = this.onLineNum + 1;
    }

    public int getOnLineNum() {
        return onLineNum;
    }

    public void setOnLineNum(int onLineNum) {
        this.onLineNum = onLineNum;
    }

    public void setRegistryList(List<String> registryList) {
        this.registryList = registryList;
    }

    public List<JobAddressVO> getRegistryInfoList() {
        return registryInfoList;
    }

    public void setRegistryInfoList(List<JobAddressVO> registryInfoList) {
        this.registryInfoList = registryInfoList;
    }
}
