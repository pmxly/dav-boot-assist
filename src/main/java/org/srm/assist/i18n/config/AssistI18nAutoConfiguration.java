package org.srm.assist.i18n.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.srm.assist.i18n.service.I18nCollectorMQClient;


@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(I18nAssistConfigProperties.class)
@EnableBinding(I18nCollectorMQClient.class)
@EnableAsync
@ConditionalOnClass(name = "org.hzero.core.redis.RedisHelper")
public class AssistI18nAutoConfiguration {

    @Bean
    public ThreadPoolTaskExecutor assistI18nAssistExecutor() {
        ThreadPoolTaskExecutor i18nAssistExecutor = new ThreadPoolTaskExecutor();
        i18nAssistExecutor.setCorePoolSize(5);
        i18nAssistExecutor.setMaxPoolSize(20);
        i18nAssistExecutor.setQueueCapacity(500);
        i18nAssistExecutor.setKeepAliveSeconds(60);
        i18nAssistExecutor.setThreadNamePrefix("assist-i18n-");
        // 拒绝策略：丢弃
        i18nAssistExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        i18nAssistExecutor.initialize();
        return i18nAssistExecutor;
    }


}
