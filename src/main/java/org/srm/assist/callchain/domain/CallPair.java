package org.srm.assist.callchain.domain;

import java.util.Objects;

public class CallPair {
    private CallPairNode caller;
    private CallPairNode callee;

    public CallPair() {
    }

    public CallPair(CallPairNode caller, CallPairNode callee) {
        this.caller = caller;
        this.callee = callee;
    }

    public CallPairNode getCaller() {
        return caller;
    }

    public void setCaller(CallPairNode caller) {
        this.caller = caller;
    }

    public CallPairNode getCallee() {
        return callee;
    }

    public void setCallee(CallPairNode callee) {
        this.callee = callee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallPair callPair = (CallPair) o;
        return Objects.equals(caller, callPair.caller) && Objects.equals(callee, callPair.callee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caller, callee);
    }
}
