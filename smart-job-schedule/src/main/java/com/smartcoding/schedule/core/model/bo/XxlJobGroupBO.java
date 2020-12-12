

package com.smartcoding.schedule.core.model.bo;

import cn.hutool.core.util.StrUtil;
import com.smartcoding.schedule.core.model.XxlJobGroup;

import java.util.List;

/**
 * @author 无缺
 * @date 2019-07-10
 */
public class XxlJobGroupBO extends XxlJobGroup {

    //执行器地址列表(系统注册)
    private List<String> registryList;

    public List<String> getRegistryList() {
        String addressList = super.getAddressList();
        registryList = StrUtil.split(addressList, ',', true, true);
        return registryList;
    }

    private List<XxlJobAddressBO> registryInfoList;
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

    public List<XxlJobAddressBO> getRegistryInfoList() {
        return registryInfoList;
    }

    public void setRegistryInfoList(List<XxlJobAddressBO> registryInfoList) {
        this.registryInfoList = registryInfoList;
    }
}
