package org.srm.assist.callchain.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

public class CallPairSet {
    @JsonIgnore
    private CallPairNode parentNode;
    private CallChainInfo chainInfo;
    private Set<CallPair> set = new HashSet<>(16);

    public CallPairSet() {
    }

    public CallPairSet(CallChainInfo chainInfo) {
        this.chainInfo = chainInfo;
    }

    public void add(CallPair callPair) {
        set.add(callPair);
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public CallPairNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(CallPairNode parentNode) {
        this.parentNode = parentNode;
    }

    public CallChainInfo getChainInfo() {
        return chainInfo;
    }

    public void setChainInfo(CallChainInfo chainInfo) {
        this.chainInfo = chainInfo;
    }

    public Set<CallPair> getSet() {
        return set;
    }

}
