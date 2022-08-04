package org.srm.assist;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "org.srm.assist")
@ConditionalOnClass(name = "org.hzero.core.redis.RedisHelper")
public class AssistAutoConfiguration {
}
