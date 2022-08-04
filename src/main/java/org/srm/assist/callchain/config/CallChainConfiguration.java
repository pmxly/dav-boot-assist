package org.srm.assist.callchain.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "srm.call.chain")
public class CallChainConfiguration implements InitializingBean {
    @Value("${spring.application.name}")
    private String innerAppName;
    @Value("${eureka.instance.metadata-map.VERSION}")
    private String version;

    private static String appName;

    private boolean enable = false;

    private String serviceBlackList;

    private String serviceWhiteList;

    private Set<String> innerServiceBlackList;

    private Set<String> innerServiceWhiteList;


    public String getVersion() {
        return version;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getServiceBlackList() {
        return serviceBlackList;
    }

    public void setServiceBlackList(String serviceBlackList) {
        this.serviceBlackList = serviceBlackList;
    }

    public String getServiceWhiteList() {
        return serviceWhiteList;
    }

    public void setServiceWhiteList(String serviceWhiteList) {
        this.serviceWhiteList = serviceWhiteList;
    }

    public static String getAppName() {
        return appName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        appName = innerAppName;
        if (!StringUtils.isEmpty(serviceBlackList)) {
            innerServiceBlackList = Arrays.stream(serviceBlackList.split(","))
                    .map(String::trim).filter(service -> !StringUtils.isEmpty(service)).collect(Collectors.toSet());
        }
        if (!StringUtils.isEmpty(serviceWhiteList)) {
            innerServiceWhiteList = Arrays.stream(serviceWhiteList.split(","))
                    .map(String::trim).filter(service -> !StringUtils.isEmpty(service)).collect(Collectors.toSet());
        }
    }

    public boolean isRecordTheCallChain() {
        if (enable) {
            if (!CollectionUtils.isEmpty(innerServiceWhiteList)) {
                return serviceWhiteList.contains(appName);
            } else if (!CollectionUtils.isEmpty(innerServiceBlackList)) {
                return !serviceBlackList.contains(appName);
            } else {
                return true;
            }
        }
        return false;
    }
}
