package org.srm.assist.callchain.autoconfig;

//import org.hzero.export.ExportAsyncTaskFinalization;
import org.hzero.export.chain.AsyncExportTaskDecorator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.srm.assist.callchain.export.CallChainExportAsyncTaskFinalization;

@Configuration
@ConditionalOnClass(name = {"org.srm.web.dynamic.DynamicContext", "org.hzero.core.export.ExportAsyncTemplate", "org.hzero.export.ExportAsyncTaskFinalization"})
@EnableFeignClients(basePackages = {"org.hzero.boot.platform.export.feign"})
public class CallChainExportConfiguration {

    @Bean
    public AsyncExportTaskDecorator exportAsyncTaskFinalization() {
        return new CallChainExportAsyncTaskFinalization();
    }
}
