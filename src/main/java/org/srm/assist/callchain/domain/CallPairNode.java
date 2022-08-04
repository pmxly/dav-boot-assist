package org.srm.assist.callchain.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class CallPairNode {
    private String className;
    private String methodName;
    private String argsString;
    private String declaringInterface;
    private String tenantNum;
    private boolean isTenant;
    @JsonIgnore
    private boolean processed;

    public CallPairNode() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getArgsString() {
        return argsString;
    }

    public void setArgsString(String argsString) {
        this.argsString = argsString;
    }

    public String getDeclaringInterface() {
        return declaringInterface;
    }

    public void setDeclaringInterface(String declaringInterface) {
        this.declaringInterface = declaringInterface;
    }

    public String getTenantNum() {
        return tenantNum;
    }

    public void setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
    }

    public boolean isTenant() {
        return isTenant;
    }

    public void setTenant(boolean tenant) {
        isTenant = tenant;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallPairNode that = (CallPairNode) o;
        return isTenant == that.isTenant && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName)
                && Objects.equals(argsString, that.argsString) && Objects.equals(tenantNum, that.tenantNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName, argsString, tenantNum, isTenant);
    }

    @Override
    public String toString() {
        return "CallPairNode{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", argsString='" + argsString + '\'' +
                ", tenantNum='" + tenantNum + '\'' +
                ", isTenant=" + isTenant +
                '}';
    }
}
