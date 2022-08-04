package org.srm.assist.callchain.domain;

import java.util.LinkedList;
import java.util.Objects;

public class CallChainContext {
    private static final ThreadLocal<CallPairSet> callPairSetTL = new ThreadLocal<>();
    private static final ThreadLocal<LinkedList<CallPairNode>> callNodeStackTL = new ThreadLocal<>();

    private CallChainContext() {
    }

    public static void addCallPairSet(CallPairSet callPairSet) {
        callPairSetTL.set(callPairSet);
    }

    public static CallPairSet getCallPairSet() {
        return callPairSetTL.get();
    }

    public static LinkedList<CallPairNode> getCallNodeStack(){
        return callNodeStackTL.get();
    }

    public static void setCallNodeStack(LinkedList<CallPairNode> callNodeStack){
        callNodeStackTL.set(callNodeStack);
    }

    public static void cleanCallPairSet() {
        callPairSetTL.remove();
    }

    public static void pushCallPairNode(CallPairNode callPairNode) {
        if (Objects.isNull(callNodeStackTL.get())) {
            callNodeStackTL.set(new LinkedList<>());
        }
        callNodeStackTL.get().push(callPairNode);
    }

    public static CallPairNode popCallPairNode() {
        LinkedList<CallPairNode> stack = callNodeStackTL.get();
        if (Objects.isNull(stack) || stack.isEmpty()) {
            return null;
        }
        return stack.pop();
    }

    public static CallPairNode peekCallPairNode() {
        LinkedList<CallPairNode> stack = callNodeStackTL.get();
        if (Objects.isNull(stack) || stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    private static void cleanCallPairNodeStack() {
        callNodeStackTL.remove();
    }

    public static void cleanContext() {
        cleanCallPairSet();
        cleanCallPairNodeStack();
    }

}
