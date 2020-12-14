package cn.smartcoding.job.core.biz.model;

import java.io.Serializable;

/**
 * Created by xuxueli on 2017-05-10 20:22:42
 */
public class JobGroupParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private String appName;
    private String title;
    private String clientVersion;

    public JobGroupParam() {
    }

    public JobGroupParam(String appName, String title, String clientVersion) {
        this.appName = appName;
        this.title = title;
        this.clientVersion = clientVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }


    @Override
    public String toString() {
        return "JobGropParam{" +
                "appName='" + appName + '\'' +
                ", title='" + title + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                '}';
    }
}
