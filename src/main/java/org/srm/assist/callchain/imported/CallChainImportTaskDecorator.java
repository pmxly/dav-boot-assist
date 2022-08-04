package org.srm.assist.callchain.imported;

import org.hzero.boot.autoconfigure.imported.ImportTaskDecorator;
import org.srm.assist.callchain.config.CallChainConfiguration;
import org.srm.assist.callchain.domain.CallChainContext;
import org.srm.assist.callchain.enhancer.CallChainInterceptor;

import javax.annotation.Resource;

public class CallChainImportTaskDecorator implements ImportTaskDecorator {

    @Resource
    private CallChainInterceptor callChainInterceptor;

    @Resource
    private CallChainConfiguration callChainConfiguration;

    @Override
    public Runnable decorate(Runnable runnable) {
        if (callChainConfiguration.isRecordTheCallChain()) {
            return () -> {
                try {
                    callChainInterceptor.before(null);
                    runnable.run();
                    callChainInterceptor.after();
                } finally {
                    // 清理调用链信息
                    CallChainContext.cleanContext();
                }
            };
        } else {
            return runnable;
        }
    }
}
