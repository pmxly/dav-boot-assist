package org.srm.assist.callchain.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

public class CallChainInfo {
    private String appName;
    private Long menuId;
    private String menuCode = "-1";
    private String url;
    private String tenantNum;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String callType;

    public CallChainInfo(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTenantNum() {
        return tenantNum;
    }

    public void setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallChainInfo that = (CallChainInfo) o;
        return Objects.equals(appName, that.appName) && Objects.equals(menuId, that.menuId)
                && Objects.equals(url, that.url) && Objects.equals(tenantNum, that.tenantNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, menuId, url, tenantNum);
    }

    @Override
    public String toString() {
        return "CallChainInfo{" +
                "appName='" + appName + '\'' +
                ", menuId=" + menuId +
                ", url='" + url + '\'' +
                ", tenantNum='" + tenantNum + '\'' +
                '}';
    }

    public interface CALL_TYPE {
        String EXPORT = "EXPORT";
        String IMPORT = "IMPORT";
    }
}