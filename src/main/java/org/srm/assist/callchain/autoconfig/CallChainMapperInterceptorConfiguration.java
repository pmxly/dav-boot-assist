package org.srm.assist.callchain.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.srm.assist.callchain.interceptor.MapperCallInterceptor;


@Configuration
@ConditionalOnClass(name = "org.apache.ibatis.plugin.Interceptor")
public class CallChainMapperInterceptorConfiguration {

    @Bean
    public MapperCallInterceptor mapperCallInterceptor() {
        return new MapperCallInterceptor();
    }
}
