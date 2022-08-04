package org.srm.assist.callchain.export;

import org.hzero.export.chain.AsyncExportTaskDecorator;
import org.srm.assist.callchain.domain.CallChainContext;
import org.srm.assist.callchain.domain.CallPairNode;
import org.srm.assist.callchain.domain.CallPairSet;
import org.srm.assist.callchain.enhancer.CallChainInterceptor;

import javax.annotation.Resource;
import java.util.LinkedList;

public class CallChainExportAsyncTaskFinalization implements AsyncExportTaskDecorator {

    @Resource
    private CallChainInterceptor callChainInterceptor;

    @Override
    public int order() {
        return 50;
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        //调用链监控拷贝线程变量
        CallPairSet callPairSet = CallChainContext.getCallPairSet();
        LinkedList<CallPairNode> callNodeStack = CallChainContext.getCallNodeStack();
        return ()->{
            try {
                //调用链监控拷贝线程变量
                if(callPairSet != null){
                    CallChainContext.addCallPairSet(callPairSet);
                }
                if(callNodeStack != null){
                    CallChainContext.setCallNodeStack(callNodeStack);
                }
                runnable.run();
                callChainInterceptor.after();
            } finally {
                CallChainContext.cleanContext();
            }
        };
    }
}
