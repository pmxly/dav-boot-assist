package org.srm.assist.i18n.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "srm.assist.i18n")
public class I18nAssistConfigProperties {
    
    private boolean collectorEnable = false;

    public boolean isCollectorEnable() {
        return collectorEnable;
    }

    public void setCollectorEnable(boolean collectorEnable) {
        this.collectorEnable = collectorEnable;
    }


}
