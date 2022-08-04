package org.srm.assist.i18n.aspect;

import io.choerodon.core.exception.ExceptionResponse;
import org.hzero.core.exception.ExceptionHandlerPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.srm.assist.common.menu.MenuHelper;
import org.srm.assist.i18n.config.I18nAssistConfigProperties;
import org.srm.assist.i18n.domain.I18nCollectorContext;
import org.srm.assist.i18n.service.I18nCollectorMQAsyncSender;

@Component
@ConditionalOnClass(name = "org.hzero.core.exception.ExceptionHandlerPostProcessor")
public class I18nExceptionPostProcessor implements ExceptionHandlerPostProcessor {

    @Autowired
    private I18nCollectorMQAsyncSender i18nCollectorMQAsyncSender;

    @Autowired
    private I18nAssistConfigProperties i18nAssistConfigProperties;

    @Override
    public void postProcessor(ExceptionResponse er, Exception ex) {
        if (!i18nAssistConfigProperties.isCollectorEnable()) {
            return;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        long menuId = MenuHelper.getMenuId(requestAttributes.getRequest());
        //没有菜单ID跳过
        if (menuId == -1) {
            return;
        }

        I18nCollectorContext i18nCollectorContext = new I18nCollectorContext();

        i18nCollectorContext.setMenuId(menuId);
        i18nCollectorContext.setType(I18nCollectorContext.TYPE_ERROR_CODE);
        i18nCollectorContext.setErrorCode(er.getCode());

        i18nCollectorMQAsyncSender.sendCollectorContext(i18nCollectorContext);

    }
}
