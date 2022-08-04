package org.srm.assist.callchain.reporter;

import org.springframework.stereotype.Component;
import org.srm.assist.callchain.reporter.checker.CallChainRepeatedChecker;
import org.srm.assist.common.marker.AssistTextMarker;

@Component
public class RedisAndMd5BasedAssistTextRepeatedChecker implements CallChainRepeatedChecker, AssistTextMarker {

    private final AssistTextMarker md5BasedAssistTextMarker;
    private final CallChainRepeatedChecker redisBasedCallChainRepeatedChecker;

    public RedisAndMd5BasedAssistTextRepeatedChecker(AssistTextMarker md5BasedAssistTextMarker, CallChainRepeatedChecker redisBasedCallChainRepeatedChecker) {
        this.md5BasedAssistTextMarker = md5BasedAssistTextMarker;
        this.redisBasedCallChainRepeatedChecker = redisBasedCallChainRepeatedChecker;
    }

    @Override
    public boolean isRepeated(String callPairSet) {
        String mark = mark(callPairSet);
        return redisBasedCallChainRepeatedChecker.isRepeated(mark);
    }

    @Override
    public String mark(String callPairSet) {
        return md5BasedAssistTextMarker.mark(callPairSet);
    }

}
