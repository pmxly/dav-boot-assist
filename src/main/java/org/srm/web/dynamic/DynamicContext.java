package org.srm.web.dynamic;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Metrics;
import org.apache.commons.lang3.StringUtils;
import org.srm.web.filter.RequestURLContext;

import java.util.Arrays;

/**
 * description
 *
 * @author David 2022/03/28 19:59
 */
public class DynamicContext {

    public static final String HEADER_TENANT_NUM = "tenantNum";
    private static final ThreadLocal<String> TENANT_NUM = new ThreadLocal();

    public DynamicContext() {
    }

    public static String getTenantNum() {
        String tenantNum = (String) TENANT_NUM.get();
        if (StringUtils.isEmpty(tenantNum)) {
            CustomUserDetails details = DetailsHelper.getUserDetails();
            if (details != null) {
                tenantNum = details.getTenantNum();
            }
        }

        return tenantNum;
    }

    public static void setTenantNum(String tenantNum) {
        TENANT_NUM.set(tenantNum);
    }

    public static void clear() {
        TENANT_NUM.remove();
    }

    public static void metrics(String tenantNum, String clazzPath) {
        try {
            String permissionId = "未经Controller调用,可能是异步功能或job";
            String controllerInfo = "未经Controller调用,可能是异步功能或job";
            if (StringUtils.isNotBlank(RequestURLContext.getMethodInfo())) {
                controllerInfo = RequestURLContext.getMethodInfo();
            }

            if (clazzPath == null) {
                clazzPath = controllerInfo;
            }

            if (RequestURLContext.getApiPermission() != null) {
                permissionId = RequestURLContext.getApiPermission().getCode();
            }

            ImmutableTag urlPath = new ImmutableTag("permissionId", permissionId);
            ImmutableTag classPath = new ImmutableTag("classPath", clazzPath);
            ImmutableTag tenant = new ImmutableTag("tenant", tenantNum);
            ImmutableTag possibleControllerPosition = new ImmutableTag("controllerInfo", controllerInfo);
            Counter counter = Metrics.counter("scux_recording", Arrays.asList(tenant, urlPath, classPath, possibleControllerPosition));
            counter.increment();
        } catch (Exception var9) {
        }

    }
}
