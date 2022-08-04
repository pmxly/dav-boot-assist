package org.srm.assist.callchain.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.srm.assist.callchain.enhancer.CallChainEnhancer;
import org.srm.assist.callchain.domain.CallChainContext;
import org.srm.assist.callchain.domain.CallPairNode;

import java.util.Objects;
import java.util.Properties;


@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
), @Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
)})
public class MapperCallInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapperCallInterceptor.class);
    private static final ClassFilterRule mapperClassFilterRule = new ClassFilterRule(new String[]{"org", "srm", "infra", "mapper"}, "Mapper");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        boolean recordCall = false;
        if (Objects.nonNull(CallChainContext.getCallPairSet()) && !CallChainContext.getCallPairSet().isEmpty()) {
            try {
                Object[] args = invocation.getArgs();
                if (Objects.nonNull(args) && args.length > 0) {
                    Object arg = args[0];
                    if (arg instanceof MappedStatement) {
                        String classAndMethod = ((MappedStatement) arg).getId();
                        if (!StringUtils.isEmpty(classAndMethod)) {
                            int i = classAndMethod.lastIndexOf(".");
                            if (i > 0) {
                                String className = classAndMethod.substring(0, i);
                                String methodName = classAndMethod.substring(i + 1);
                                if (mapperClassFilterRule.match(className)) {
                                    CallPairNode currentNode = new CallPairNode();
                                    currentNode.setClassName(className);
                                    currentNode.setMethodName(methodName);
                                    // mapper方法不会重载，不记录具体方法签名
                                    currentNode.setArgsString("");
                                    // mapper方法不记录声明接口
                                    currentNode.setDeclaringInterface("");
                                    CallChainEnhancer.before(currentNode);
                                    recordCall = true;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("记录调用链异常", e);
            }
        }
        Object proceed = invocation.proceed();
        if (recordCall) {
            CallChainEnhancer.after();
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
