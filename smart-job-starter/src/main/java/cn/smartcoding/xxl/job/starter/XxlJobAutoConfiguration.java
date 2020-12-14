package cn.smartcoding.xxl.job.starter;

import cn.smartcoding.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author 无缺
 * @date 2019-07-28
 */
@Configuration
@EnableConfigurationProperties(XxxJobProperties.class)
public class XxlJobAutoConfiguration {
    private Logger logger = LoggerFactory.getLogger(XxlJobAutoConfiguration.class);

    @Resource
    private XxxJobProperties xxxJobProperties;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    @ConditionalOnMissingBean(XxlJobSpringExecutor.class)
    public XxlJobSpringExecutor xxlJobExecutor() {

        XxxJobProperties.Executor executor = xxxJobProperties.getExecutor();
        if (executor == null) {
            logger.error(">>>>>>>>>>> xxl-job config init fail,executor not null");
            return null;
        }
        if (executor.getAppName() == null || executor.getAppName().isEmpty()) {
            logger.error(">>>>>>>>>>> xxl-job config init fail,executor app name not null");
            return null;
        }

        String title = executor.getTitle();
        String appName = executor.getAppName();
        boolean autoCreateJob = executor.isAutoCreateJob();
        boolean autoStartJob = executor.isAutoStartJob();
        String adminAddresses = xxxJobProperties.getAdminAddresses();
        String accessToken = xxxJobProperties.getAccessToken();
        logger.info(">>>>>>>>>>> xxl-job config init,appName:{},port:{},addresses:{},autoCreateJob:{},autoStartJob:{}", appName, executor.getPort(), adminAddresses, autoCreateJob, autoStartJob);
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppName(appName);
        if (title == null) {
            xxlJobSpringExecutor.setTitle(appName);
        } else {
            xxlJobSpringExecutor.setTitle(title);
        }
        xxlJobSpringExecutor.setAutoStartJob(autoStartJob);
        xxlJobSpringExecutor.setAutoCreateJob(autoCreateJob);
        xxlJobSpringExecutor.setIp(executor.getIp());
        xxlJobSpringExecutor.setPort(executor.getPort());
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(executor.getLogPath());
        if (executor.getLogRetentionDays() < 1 && executor.getLogRetentionDays() > 20) {
            logger.info(">>>>>>>>>>> xxl-job config init,logRetentionDays more than range [1,20],logRetentionDays:{},reset logRetentionDays=7", executor.getLogRetentionDays());
            xxlJobSpringExecutor.setLogRetentionDays(7);
        } else {
            xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        }
        return xxlJobSpringExecutor;
    }
}
