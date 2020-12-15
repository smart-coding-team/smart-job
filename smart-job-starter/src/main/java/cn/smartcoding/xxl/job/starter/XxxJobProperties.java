package cn.smartcoding.xxl.job.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *  xxxJobProperties
 * @author 无缺
 */
@ConfigurationProperties(prefix = "xxl.job")
public class XxxJobProperties {

    private String adminAddresses;

    private String accessToken;

    private Executor executor;


    public String getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public static class Executor {
        private String appName;

        private String title;

        private boolean autoCreateJob;

        private boolean autoStartJob;

        private String ip;

        private int port = 9919;

        private String logPath="/data/logs/xxl-job/jobHandler";

        private int logRetentionDays =7;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getLogPath() {
            return logPath;
        }

        public void setLogPath(String logPath) {
            this.logPath = logPath;
        }

        public int getLogRetentionDays() {
            return logRetentionDays;
        }

        public void setLogRetentionDays(int logRetentionDays) {
            this.logRetentionDays = logRetentionDays;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isAutoCreateJob() {
            return autoCreateJob;
        }

        public void setAutoCreateJob(boolean autoCreateJob) {
            this.autoCreateJob = autoCreateJob;
        }

        public boolean isAutoStartJob() {
            return autoStartJob;
        }

        public void setAutoStartJob(boolean autoStartJob) {
            this.autoStartJob = autoStartJob;
        }

        @Override
        public String toString() {
            return "Executor{" +
                    "appName='" + appName + '\'' +
                    ", title='" + title + '\'' +
                    ", autoCreateJob=" + autoCreateJob +
                    ", autoStartJob=" + autoStartJob +
                    ", ip='" + ip + '\'' +
                    ", port=" + port +
                    ", logPath='" + logPath + '\'' +
                    ", logRetentionDays=" + logRetentionDays +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "XxxJobProperties{" +
                "adminAddresses='" + adminAddresses + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", executor=" + executor +
                '}';
    }
}
