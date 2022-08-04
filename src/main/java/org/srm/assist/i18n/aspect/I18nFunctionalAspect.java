package org.srm.assist.i18n.aspect;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.srm.assist.common.menu.MenuHelper;
import org.srm.assist.i18n.config.I18nAssistConfigProperties;
import org.srm.assist.i18n.domain.I18nCollectorContext;
import org.srm.assist.i18n.service.I18nCollectorMQAsyncSender;

import java.util.Map;

/**
 * @author qixiangyu@going-link.com
 */
@Aspect
@Component
@ConditionalOnProperty(name = "spring.application.name", havingValue = "hzero-platform")
public class I18nFunctionalAspect {


    private static final Logger LOGGER = LoggerFactory.getLogger(I18nFunctionalAspect.class);

    @Autowired
    private I18nCollectorMQAsyncSender i18nCollectorMQAsyncSender;

    @Autowired
    private I18nAssistConfigProperties i18nAssistConfigProperties;

    /**
     * 拦截多语言
     */
    @Pointcut("execution(* org.hzero.platform.domain.repository.PromptRepository.getDescription(..))")
    public void getDescription() {
    }

    /**
     * 拦截值集视图查询
     */
    @Pointcut("execution(* org.hzero.platform.app.service.LovViewHeaderService.queryLovViewInfo(..))")
    public void getLovView() {
    }

    /**
     * 拦截值集视图查询(批量)
     */
    @Pointcut("execution(* org.hzero.platform.app.service.LovValueService.batchQueryLovValue(..))")
    public void getBatchLovValue() {
    }

    /**
     * 拦截值集视图查询
     */
    @Pointcut("execution(* org.hzero.platform.app.service.LovValueService.queryLovValue(..))")
    public void getLovValue() {
    }


    /**
     * 记录多语言描述与菜单的关系
     *
     * @param joinPoint
     */
    @After("getDescription()")
    public void tenantDescription(JoinPoint joinPoint) {
        if (!i18nAssistConfigProperties.isCollectorEnable()) {
            return;
        }
        try {
            Object[] objects = joinPoint.getArgs();
            if (objects == null || objects.length == 0) {
                return;
            }
            Long tenantId = (Long) objects[2];
            String[] promptKey = (String[]) objects[0];

            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            long menuId = MenuHelper.getMenuId(requestAttributes.getRequest());
            //没有菜单ID跳过
            if (menuId == -1) {
                return;
            }

            I18nCollectorContext i18nCollectorContext = new I18nCollectorContext();
            i18nCollectorContext.setPromptKey(promptKey);
            i18nCollectorContext.setTenantId(tenantId);
            i18nCollectorContext.setMenuId(menuId);
            i18nCollectorContext.setType(I18nCollectorContext.TYPE_PROMOTE);

            i18nCollectorMQAsyncSender.sendCollectorContext(i18nCollectorContext);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 记录值集视图与菜单的关系
     *
     * @param joinPoint
     */
    @After("getLovView()")
    public void tenantLovView(JoinPoint joinPoint) {
        if (!i18nAssistConfigProperties.isCollectorEnable()) {
            return;
        }
        try {
            Object[] objects = joinPoint.getArgs();
            if (objects == null || objects.length == 0) {
                return;
            }
            Long tenantId = (Long) objects[1];
            String lovViewCode = (String) objects[0];

            if (lovViewCode == null) {
                return;
            }

            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            long menuId = MenuHelper.getMenuId(requestAttributes.getRequest());
            //没有菜单ID跳过
            if (menuId == -1) {
                return;
            }

            I18nCollectorContext i18nCollectorContext = new I18nCollectorContext();
            i18nCollectorContext.setLovViewCode(lovViewCode);
            i18nCollectorContext.setTenantId(tenantId);
            i18nCollectorContext.setMenuId(menuId);
            i18nCollectorContext.setType(I18nCollectorContext.TYPE_LOV_VIEW);

            i18nCollectorMQAsyncSender.sendCollectorContext(i18nCollectorContext);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 记录值集视图与菜单的关系
     *
     * @param joinPoint
     */
    @After("getBatchLovValue()")
    public void batchLovValue(JoinPoint joinPoint) {
        if (!i18nAssistConfigProperties.isCollectorEnable()) {
            return;
        }
        try {
            Object[] objects = joinPoint.getArgs();
            if (objects == null || objects.length == 0) {
                return;
            }
            Long tenantId = (Long) objects[1];
            Map<String, String> queryMap = (Map<String, String>) objects[0];
            if (MapUtils.isEmpty(queryMap)) {
                return;
            }

            String[] lovCodes = queryMap.values().toArray(new String[0]);

            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            long menuId = MenuHelper.getMenuId(requestAttributes.getRequest());
            //没有菜单ID跳过
            if (menuId == -1) {
                return;
            }

            I18nCollectorContext i18nCollectorContext = new I18nCollectorContext();
            i18nCollectorContext.setLovCodes(lovCodes);
            i18nCollectorContext.setTenantId(tenantId);
            i18nCollectorContext.setMenuId(menuId);
            i18nCollectorContext.setType(I18nCollectorContext.TYPE_LOV_VALUE);

            i18nCollectorMQAsyncSender.sendCollectorContext(i18nCollectorContext);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 记录值集视图与菜单的关系
     *
     * @param joinPoint
     */
    @After("getLovValue()")
    public void getLovValue(JoinPoint joinPoint) {
        if (!i18nAssistConfigProperties.isCollectorEnable()) {
            return;
        }
        try {
            Object[] objects = joinPoint.getArgs();
            if (objects == null || objects.length == 0) {
                return;
            }
            Long tenantId = (Long) objects[1];
            String lovCode = (String) objects[0];
            if (StringUtils.isBlank(lovCode)) {
                return;
            }


            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            long menuId = MenuHelper.getMenuId(requestAttributes.getRequest());
            //没有菜单ID跳过
            if (menuId == -1) {
                return;
            }

            I18nCollectorContext i18nCollectorContext = new I18nCollectorContext();
            i18nCollectorContext.setLovCodes(new String[]{lovCode});
            i18nCollectorContext.setTenantId(tenantId);
            i18nCollectorContext.setMenuId(menuId);
            i18nCollectorContext.setType(I18nCollectorContext.TYPE_LOV_VALUE);

            i18nCollectorMQAsyncSender.sendCollectorContext(i18nCollectorContext);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


}
