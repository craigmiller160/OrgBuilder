package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by craig on 11/4/16.
 */
@XmlRootElement
public class AppInfoDTO {

    private String appName;
    private String appVersion;

    public AppInfoDTO(){}

    public AppInfoDTO(String appName, String appVersion) {
        this.appName = appName;
        this.appVersion = appVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppInfoDTO that = (AppInfoDTO) o;

        if (appName != null ? !appName.equals(that.appName) : that.appName != null) return false;
        return appVersion != null ? appVersion.equals(that.appVersion) : that.appVersion == null;

    }

    @Override
    public int hashCode() {
        int result = appName != null ? appName.hashCode() : 0;
        result = 31 * result + (appVersion != null ? appVersion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return appName + " " + appVersion;
    }
}
