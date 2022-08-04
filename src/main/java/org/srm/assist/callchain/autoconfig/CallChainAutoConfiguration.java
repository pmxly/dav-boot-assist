package org.srm.assist.callchain.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.srm.assist.callchain.config.CallChainConfiguration;
import org.srm.assist.callchain.domain.CallChainInfo;
import org.srm.assist.callchain.enhancer.CallChainInterceptor;
import org.srm.assist.callchain.reporter.CallChainMQAsyncSender;
import org.srm.assist.callchain.reporter.CallChainMQClient;

import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(CallChainConfiguration.class)
@EnableBinding(CallChainMQClient.class)
@EnableAsync
@ConditionalOnClass(name = "org.hzero.core.redis.RedisHelper")
public class CallChainAutoConfiguration {

    @Bean
    public ThreadPoolTaskExecutor callChainMQSendExecutor() {
        ThreadPoolTaskExecutor callChainMQSendExecutor = new ThreadPoolTaskExecutor();
        callChainMQSendExecutor.setCorePoolSize(2);
        callChainMQSendExecutor.setMaxPoolSize(4);
        callChainMQSendExecutor.setQueueCapacity(500);
        callChainMQSendExecutor.setKeepAliveSeconds(60);
        callChainMQSendExecutor.setThreadNamePrefix("srm-call-chain-sendMQ-");
        // 拒绝策略：丢弃
        callChainMQSendExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        callChainMQSendExecutor.initialize();
        return callChainMQSendExecutor;
    }

    @Bean
    public CallChainMQAsyncSender callChainMQAsyncSender() {
        return new CallChainMQAsyncSender();
    }

    @Bean
    public CallChainInterceptor callChainInterceptor(CallChainConfiguration callChainConfiguration) {
        String appName = callChainConfiguration.getAppName();
        CallChainInfo callChainInfo = new CallChainInfo(appName);
        return new CallChainInterceptor(callChainInfo);
    }

}
