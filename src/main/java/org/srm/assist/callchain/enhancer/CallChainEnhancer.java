package org.srm.assist.callchain.enhancer;

import io.hcbm.starter.web.annotation.Tenant;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.export.annotation.ExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.srm.assist.callchain.config.CallChainConfiguration;
import org.srm.assist.callchain.domain.*;
//import org.srm.web.annotation.Tenant;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class CallChainEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallChainEnhancer.class);

    private static boolean existExcelExport = true;
    private static boolean existImportService = true;

    static {
        try {
            Class.forName("org.hzero.export.annotation.ExcelExport");
        } catch (ClassNotFoundException e) {
            existExcelExport = false;
        }

        try {
            Class.forName("org.hzero.boot.imported.infra.validator.annotation.ImportService");
        } catch (ClassNotFoundException e) {
            existImportService = false;
        }
    }

    private CallChainEnhancer() {
    }

    public static void before(CallPairNode currentNode) {
        try {
            if (Objects.isNull(CallChainContext.getCallPairSet())) {
                return;
            }
            CallPairSet callPairSet = CallChainContext.getCallPairSet();
            CallPairNode parentNode = CallChainContext.peekCallPairNode();
            callPairSet.add(new CallPair(parentNode, currentNode));
            CallChainContext.pushCallPairNode(currentNode);
        } catch (Exception e) {
            LOGGER.warn("记录调用链异常", e);
        }
    }

    public static void processNode(CallPairNode callPairNode, CallChainInfo chainInfo) throws ClassNotFoundException {
        if (!callPairNode.isProcessed()) {
            Class<?> currentClazz = getRealClass(callPairNode.getClassName());
            if (Objects.nonNull(currentClazz)) {
                boolean isTenant = deduceIsTenant(currentClazz);
                if (isTenant) {
                    callPairNode.setTenant(true);
                    callPairNode.setTenantNum(chainInfo.getTenantNum());
                }
                // 感知导出链路
                if (existExcelExport) {
                    perceiveExport(currentClazz, callPairNode, chainInfo);
                }
                // 感知导入链路
                if (existImportService) {
                    perceiveImport(currentClazz, callPairNode, chainInfo);
                }
            }

            callPairNode.setProcessed(true);
        }
    }

    private static void perceiveExport(Class<?> currentClazz, CallPairNode callPairNode, CallChainInfo chainInfo) {
        RestController controller = AnnotationUtils.findAnnotation(currentClazz, RestController.class);
        if (Objects.nonNull(controller)) {
            Method[] methods = currentClazz.getMethods();
            try {
                for (Method method : methods) {

                    // 1. 方法名相同
                    if (Objects.equals(callPairNode.getMethodName(), method.getName())) {
                        // 2. 有ExcelExport注解
                        ExcelExport excelExport = method.getAnnotation(ExcelExport.class);
                        if (Objects.nonNull(excelExport)) {
                            // 3. 记录到chainInfo中
                            chainInfo.setCallType(CallChainInfo.CALL_TYPE.EXPORT);
                        }

                    }

                }
            } catch (NoClassDefFoundError error) {
                // do nothing
            }

        }
    }

    private static void perceiveImport(Class<?> currentClazz, CallPairNode callPairNode, CallChainInfo chainInfo) {
        try {
            ImportService importService = AnnotationUtils.findAnnotation(currentClazz, ImportService.class);
            if (Objects.nonNull(importService)) {
                chainInfo.setCallType(CallChainInfo.CALL_TYPE.IMPORT);
            }
        } catch (NoClassDefFoundError error) {
            // do nothing
        }
    }

    private static String genUrlBy(Class<?> currentClazz, Method method) {
        String controllerUrl = "";
        String methodUrl = "";
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(currentClazz, RequestMapping.class);
        if (Objects.nonNull(requestMapping)) {
            controllerUrl = requestMapping.value().length > 0 ? requestMapping.value()[0] : "";
        }
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        if (Objects.nonNull(getMapping)) {
            methodUrl = getMapping.value().length > 0 ? getMapping.value()[0] : "";
            return String.format("%s %s%s", RequestMethod.GET, controllerUrl, methodUrl);
        }
        DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        if (Objects.nonNull(deleteMapping)) {
            methodUrl = deleteMapping.value().length > 0 ? deleteMapping.value()[0] : "";
            return String.format("%s %s%s", RequestMethod.DELETE, controllerUrl, methodUrl);
        }
        PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
        if (Objects.nonNull(postMapping)) {
            methodUrl = postMapping.value().length > 0 ? postMapping.value()[0] : "";
            return String.format("%s %s%s", RequestMethod.POST, controllerUrl, methodUrl);
        }
        PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
        if (Objects.nonNull(putMapping)) {
            methodUrl = putMapping.value().length > 0 ? putMapping.value()[0] : "";
            return String.format("%s %s%s", RequestMethod.PUT, controllerUrl, methodUrl);
        }
        RequestMapping methodRequestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        if (Objects.nonNull(methodRequestMapping)) {
            methodUrl = methodRequestMapping.value().length > 0 ? methodRequestMapping.value()[0] : "";
            RequestMethod requestMethod = methodRequestMapping.method().length > 0 ? methodRequestMapping.method()[0] : RequestMethod.GET;
            return String.format("%s %s%s", requestMethod, controllerUrl, methodUrl);
        }
        return null;
    }

    public static void after() {
        try {
            if (Objects.isNull(CallChainContext.getCallPairSet())) {
                return;
            }
            // 方法调用完成后pop
            CallChainContext.popCallPairNode();
        } catch (Exception e) {
            LOGGER.warn("记录调用链异常", e);
        }
    }

    private static Class<?> getRealClass(String clazzName) throws ClassNotFoundException {
        Class<?> currentClazz = Class.forName(clazzName);

        // 非spring进行的jdk动态代理处理
        // 比如：mapper的处理
        if (Proxy.isProxyClass(currentClazz)) {
            // 取jdk动态代理第一个接口
            currentClazz = currentClazz.getInterfaces()[0];
            return currentClazz;
        }

        // cglib动态代理，取第一个不是cglib动态代理的父类
        while (Objects.nonNull(currentClazz) && ClassUtils.isCglibProxyClass(currentClazz)) {
            currentClazz = currentClazz.getSuperclass();
        }
        return currentClazz;
    }

    private static boolean deduceIsTenant(Class<?> currentClazz) {
        Tenant tenant = AnnotationUtils.findAnnotation(currentClazz, Tenant.class);
        boolean isTenant = false;
        String path;
        String serviceKey = CallChainConfiguration.getAppName() + "-";
        if (Objects.nonNull(tenant)) {
            isTenant = true;
        } else {
            if (serviceKey.startsWith("hzero")) {
                return false;
            }
            path = getClassFilePath(currentClazz);
            if (path.contains(serviceKey)) {
                isTenant = true;
            }
        }
        return isTenant;
    }

    private static String getClassFilePath(Class<?> currentClazz) {
        return currentClazz.getResource(currentClazz.getSimpleName() + ".class").getPath();
    }
}
