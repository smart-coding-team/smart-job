

package cn.smartcoding.schedule.service;


import cn.smartcoding.schedule.core.model.XxlJobInfo;
import cn.smartcoding.schedule.core.model.bo.JobDashbordBO;
import cn.smartcoding.schedule.core.model.bo.XxlJobInfoBO;
import cn.smartcoding.schedule.core.model.bo.XxlJobStatisticBO;
import cn.smartcoding.schedule.core.model.bo.XxlShortJobInfoBO;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * com.smartcoding.robot.core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {

    /**
     * page list
     *
     * @param pageNum
     * @param pageSize
     * @param jobGroup
     * @param jobStatus
     * @param glueType
     * @param id
     * @return
     */
    PageInfo<XxlJobInfoBO> pageList(int pageNum, int pageSize, Long id, Long jobGroup, String glueType, Integer jobStatus, Integer triggerStatus);

    /**
     * add job
     *
     * @param jobInfo
     * @return
     */
    Long add(XxlJobInfo jobInfo);

    /**
     * update job
     *
     * @param jobInfo
     * @return
     */
    int update(XxlJobInfo jobInfo);

    /**
     * remove job
     * *
     *
     * @param id
     * @return
     */
    int remove(Long id);

    /**
     * start job
     *
     * @param id
     * @return
     */
    int start(Long id);

    /**
     * stop job
     *
     * @param id
     * @return
     */
    int stop(Long id);

    /**
     * dashboard info
     *
     * @return
     */
    JobDashbordBO dashboardInfo();

    /**
     * chart info
     *
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String, Object> chartInfo(Date startDate, Date endDate);

    XxlJobInfo getJobDetialById(Long id);

    XxlJobStatisticBO getXxlJobStatisticBO();

    List<XxlShortJobInfoBO> getXxlShortJobInfoBO(String searchContent, int pageSize);

    List<XxlShortJobInfoBO> selectJobInfoByIdList(List<Long> jobIdList);

    List<XxlShortJobInfoBO> getXxlShortJobInfoBO(int nextMinutes);

    List<XxlShortJobInfoBO> getXxlShortJobInfoBO(long startSecondsMilli, long endSecondsMilli);

    XxlJobInfo getXxxJobInfo(Long jobGroup, String executorHandler);

    void changeJobCreateWay(Long id, Integer status);

    void changeJobStatus(Long id, Integer status);

    Long copy(Long id, String username);
}
