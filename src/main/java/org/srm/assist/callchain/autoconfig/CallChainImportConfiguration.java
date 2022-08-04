package org.srm.assist.callchain.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.srm.assist.callchain.imported.CallChainImportTaskDecorator;

@Configuration
@ConditionalOnClass(name = {"org.srm.web.dynamic.DynamicContext", "org.hzero.boot.autoconfigure.imported.ImportTaskDecorator"})
public class CallChainImportConfiguration {
    @Bean
    public CallChainImportTaskDecorator callChainImportTaskDecorator() {
        return new CallChainImportTaskDecorator();
    }
}
