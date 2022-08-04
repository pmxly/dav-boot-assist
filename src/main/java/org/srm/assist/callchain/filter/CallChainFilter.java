package org.srm.assist.callchain.filter;

import org.srm.assist.callchain.config.CallChainConfiguration;
import org.srm.assist.callchain.domain.CallChainContext;
import org.srm.assist.callchain.enhancer.CallChainInterceptor;

import javax.annotation.Resource;
import javax.servlet.*;
import java.io.IOException;

public class CallChainFilter implements Filter {

    @Resource
    private CallChainConfiguration callChainConfiguration;

    @Resource
    private CallChainInterceptor callChainInterceptor;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (callChainConfiguration.isRecordTheCallChain()) {
            try {
                beforeDoFilter(servletRequest);
                filterChain.doFilter(servletRequest, servletResponse);
                postDoFilter();
            } finally {
                // 清理调用链信息
                CallChainContext.cleanContext();
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void beforeDoFilter(ServletRequest servletRequest) {
        callChainInterceptor.before(servletRequest);
    }

    private void postDoFilter() {
        callChainInterceptor.after();
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
