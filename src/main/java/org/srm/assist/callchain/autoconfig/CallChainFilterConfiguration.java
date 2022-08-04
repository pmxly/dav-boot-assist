package org.srm.assist.callchain.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.srm.assist.callchain.filter.CallChainFilter;

@Configuration
//@ConditionalOnClass(name = "org.srm.web.dynamic.DynamicContext")
public class CallChainFilterConfiguration {

    @Bean
    public CallChainFilter callChainFilter() {
        return new CallChainFilter();
    }

}
