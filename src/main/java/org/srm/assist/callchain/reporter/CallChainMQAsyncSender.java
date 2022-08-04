package org.srm.assist.callchain.reporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;
import org.srm.assist.callchain.domain.CallChainInfo;
import org.srm.assist.callchain.domain.CallPair;
import org.srm.assist.callchain.domain.CallPairNode;
import org.srm.assist.callchain.domain.CallPairSet;
import org.srm.assist.callchain.enhancer.CallChainEnhancer;
import org.srm.assist.common.menu.MenuHelper;

import java.util.Objects;
import java.util.Set;

public class CallChainMQAsyncSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallChainMQAsyncSender.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CallChainMQClient callChainMQClient;

    @Autowired
    @Lazy
    private MenuHelper menuHelper;

    @Autowired
    private RedisAndMd5BasedAssistTextRepeatedChecker callChainRepeatedChecker;

    @Async("callChainMQSendExecutor")
    public void processAndSend(CallPairSet callPairSet) {
        try {
            process(callPairSet);
            send(callPairSet);
        } catch (Exception e) {
            LOGGER.warn("调用链发送异常", e);
        }
    }

    private void process(CallPairSet callPairSet) throws ClassNotFoundException {
        CallChainInfo chainInfo = callPairSet.getChainInfo();
        Long menuId = chainInfo.getMenuId();
        if (menuId != -1) {
            menuHelper.getMenuCodeById(menuId).ifPresent(chainInfo::setMenuCode);
        }
        Set<CallPair> set = callPairSet.getSet();
        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        for (CallPair callPair : set) {
            CallPairNode callee = callPair.getCallee();
            CallPairNode caller = callPair.getCaller();
            if (Objects.nonNull(callee)) {
                CallChainEnhancer.processNode(callee, chainInfo);
            }
            if (Objects.nonNull(caller)) {
                CallChainEnhancer.processNode(caller, chainInfo);
            }
        }

        boolean notPresentTenantNode = true;

        for (CallPair callPair : set) {
            CallPairNode callee = callPair.getCallee();
            CallPairNode caller = callPair.getCaller();
            if ((Objects.nonNull(callee) && callee.isTenant()) || (Objects.nonNull(caller) && caller.isTenant())) {
                notPresentTenantNode = false;
                break;
            }
        }

        if (notPresentTenantNode) {
            chainInfo.setTenantNum("0");
        }
    }

    private void send(CallPairSet callPairSet) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(callPairSet);
        if (callChainRepeatedChecker.isRepeated(message)) {
            return;
        }
        callChainMQClient.output().send(MessageBuilder.withPayload(message).build());
        LOGGER.debug("发送调用链信息,callInfo:{}", message);
    }
}
