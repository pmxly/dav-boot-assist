package org.srm.assist.callchain.reporter.checker;

import org.hzero.core.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RedisBasedCallChainRepeatedChecker implements CallChainRepeatedChecker {

    private static final String CALL_CHAIN_CACHE_KEY = "sast:call_chain";
    private static final String CALL_CHAIN_VERSION_KEY = "sast:call_chain:version";

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public boolean isRepeated(String callChainMark) {
        String currentVersion = redisHelper.strGet(CALL_CHAIN_VERSION_KEY);
        if (Objects.isNull(currentVersion)) {
            currentVersion = "1";
        }
        String redisCallChainResult = redisHelper.hshGet(CALL_CHAIN_CACHE_KEY, callChainMark);

        // 当前迭代已经处理过该调用
        return Objects.nonNull(redisCallChainResult) && Objects.equals(currentVersion, redisCallChainResult);
    }
}
