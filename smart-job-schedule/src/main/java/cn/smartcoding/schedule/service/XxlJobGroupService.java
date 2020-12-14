

package cn.smartcoding.schedule.service;

import cn.smartcoding.schedule.core.model.XxlJobGroup;
import cn.smartcoding.schedule.core.model.vo.JobAddressGroupVO;
import cn.smartcoding.schedule.core.model.vo.JobGroupVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface XxlJobGroupService {

    JobAddressGroupVO getXxlJobAddressBOS(List<String> registryList, String appName);

    int getOnLineNum(List<String> registryList, String appName);

    PageInfo<JobGroupVO> pageList(int pageNum, int pageSize, Long id, Integer addressType, Integer alarmStatus);

    int addJobGroup(XxlJobGroup jobGroup);

    int update(XxlJobGroup jobGroupVO);

    int remove(Long id);

    XxlJobGroup loadById(Long id);

    JobAddressGroupVO getOnLineAddressList(Long id);

    List<XxlJobGroup> findAll();

    List<XxlJobGroup> querySearch(String title, int pageSize);
}
