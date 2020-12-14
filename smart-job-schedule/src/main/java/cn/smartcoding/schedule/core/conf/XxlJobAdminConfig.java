

package cn.smartcoding.schedule.core.conf;

import cn.smartcoding.job.core.biz.AdminBiz;
import cn.smartcoding.job.core.biz.JobBiz;
import cn.smartcoding.schedule.core.alarm.JobAlarm;
import cn.smartcoding.schedule.mapper.XxlJobGroupMapper;
import cn.smartcoding.schedule.mapper.XxlJobInfoMapper;
import cn.smartcoding.schedule.mapper.XxlJobLogMapper;
import cn.smartcoding.schedule.mapper.XxlJobRegistryMapper;
import cn.smartcoding.schedule.service.IXxlJobAlarmInfoService;
import cn.smartcoding.schedule.service.IXxlJobAlarmLogService;
import cn.smartcoding.schedule.service.IXxlJobTreadService;
import cn.smartcoding.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Collection;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
public class XxlJobAdminConfig implements InitializingBean {
    private Logger log = LoggerFactory.getLogger(XxlJobAdminConfig.class);
    private static XxlJobAdminConfig adminConfig = null;

    public static XxlJobAdminConfig getAdminConfig() {
        return adminConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(" >>>>>>>>>>>  init adminConfig");
        adminConfig = this;
    }

    // conf
    @Value("${xxl.job.i18n}")
    private String i18n;

    @Value("${xxl.job.accessToken}")
    private String accessToken;
    @Value("${xxl.job.applicationName}")
    private String applicationName;

    @Value("${xxl.job.triggerpool.fastMax:200}")
    private int triggerPoolFastMax;

    @Value("${xxl.job.triggerpool.slowMax:100}")
    private int triggerPoolSlowMax;


    // mapper, service

    @Resource
    private XxlJobLogMapper xxlJobLogDao;
    @Resource
    private XxlJobInfoMapper xxlJobInfoDao;
    @Resource
    private XxlJobRegistryMapper xxlJobRegistryDao;
    @Resource
    private XxlJobGroupMapper xxlJobGroupDao;
    @Resource
    private AdminBiz adminBiz;
    @Resource
    private JobBiz jobBiz;
    @Resource
    private DataSource dataSource;
    @Resource
    private IXxlJobAlarmInfoService xxlJobAlarmInfoService;
    @Resource
    private IXxlJobAlarmLogService xxlJobAlarmLogService;
    @Resource
    private Collection<JobAlarm> jobAlarms;
    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IXxlJobTreadService xxlJobScheduleService;

    public String getI18n() {
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }


    public XxlJobLogMapper getXxlJobLogDao() {
        return xxlJobLogDao;
    }

    public XxlJobInfoMapper getXxlJobInfoDao() {
        return xxlJobInfoDao;
    }

    public XxlJobRegistryMapper getXxlJobRegistryDao() {
        return xxlJobRegistryDao;
    }

    public XxlJobGroupMapper getXxlJobGroupDao() {
        return xxlJobGroupDao;
    }

    public AdminBiz getAdminBiz() {
        return adminBiz;
    }

    public JobBiz getJobBiz() {
        return jobBiz;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getApplicationName() {
        return applicationName;
    }


    public void setApplicationName(String applicationName) {

        this.applicationName = applicationName;
    }

    public int getTriggerPoolFastMax() {
        return triggerPoolFastMax;
    }

    public void setTriggerPoolFastMax(int triggerPoolFastMax) {
        this.triggerPoolFastMax = triggerPoolFastMax;
    }

    public int getTriggerPoolSlowMax() {
        return triggerPoolSlowMax;
    }

    public void setTriggerPoolSlowMax(int triggerPoolSlowMax) {
        this.triggerPoolSlowMax = triggerPoolSlowMax;
    }


    public IXxlJobTreadService getXxlJobScheduleService() {
        return xxlJobScheduleService;
    }

    public IXxlJobAlarmInfoService getXxlJobAlarmInfoService() {
        return xxlJobAlarmInfoService;
    }

    public IXxlJobAlarmLogService getXxlJobAlarmLogService() {
        return xxlJobAlarmLogService;
    }

    public Collection<JobAlarm> getJobAlarms() {
        return jobAlarms;
    }

    public ISysUserService getSysUserService() {
        return sysUserService;
    }
}
