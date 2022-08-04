package org.srm.assist.callchain.enhancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.srm.assist.callchain.domain.CallChainContext;
import org.srm.assist.callchain.domain.CallChainInfo;
import org.srm.assist.callchain.domain.CallPairSet;
import org.srm.assist.callchain.reporter.CallChainMQAsyncSender;
import org.srm.assist.common.menu.MenuHelper;
import org.srm.web.dynamic.DynamicContext;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.Objects;

public class CallChainInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallChainInterceptor.class);

    private final CallChainInfo baseCallChainInfo;

    @Resource
    private CallChainMQAsyncSender callChainMQAsyncSender;

    public CallChainInterceptor(CallChainInfo baseCallChainInfo) {
        this.baseCallChainInfo = baseCallChainInfo;
    }

    public void before(ServletRequest servletRequest) {
        try {
            if (Objects.isNull(servletRequest)) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (Objects.nonNull(requestAttributes)) {
                    servletRequest = requestAttributes.getRequest();
                }
            }
            // 添加调用链信息
            CallChainInfo callChainInfo = new CallChainInfo(baseCallChainInfo.getAppName());
            long menuId = MenuHelper.getMenuId(servletRequest);
            callChainInfo.setMenuId(menuId);
            callChainInfo.setTenantNum(getTenantNum());
            CallChainContext.addCallPairSet(new CallPairSet(callChainInfo));
        } catch (Exception e) {
            LOGGER.warn("调用链记录异常", e);
        }
    }

    public void after() {
        try {
            // 异步填充调用节点必要信息，并发送消息传输调用链信息
            if (Objects.nonNull(CallChainContext.getCallPairSet())) {
                if (!CallChainContext.getCallPairSet().isEmpty()) {
                    CallChainContext.getCallPairSet().getChainInfo().setUrl(getUrl());
                    callChainMQAsyncSender.processAndSend(CallChainContext.getCallPairSet());
                }
            }
        } catch (Exception e) {
            LOGGER.warn("调用链记录异常", e);
        }
    }

    private String getTenantNum() {
        return DynamicContext.getTenantNum();
    }

    private String getUrl() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return null;
        }
        String method = requestAttributes.getRequest().getMethod();
        String urlPath = (String) requestAttributes.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

        return String.format("%s %s", method, urlPath);
    }
}
