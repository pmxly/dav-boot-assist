package org.srm.assist.i18n.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hzero.core.redis.RedisHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.srm.assist.callchain.reporter.CallChainMQAsyncSender;
import org.srm.assist.common.marker.Md5BasedAssistTextMarker;
import org.srm.assist.common.menu.MenuHelper;
import org.srm.assist.i18n.domain.I18nCollectorContext;

@Component
public class I18nCollectorMQAsyncSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallChainMQAsyncSender.class);

    private static final String I18N_CACHE_KEY = "sast:i18n:";

    @Autowired
    private I18nCollectorMQClient mqClient;

    @Autowired
    private MenuHelper menuHelper;

    @Autowired
    private Md5BasedAssistTextMarker assistTextMarker;

    @Autowired
    private RedisHelper redisHelper;


    @Async("assistI18nAssistExecutor")
    public void sendCollectorContext(I18nCollectorContext i18nCollectorContext) {

        if (i18nCollectorContext.getMenuId() == null || ObjectUtils.nullSafeEquals(-1L,i18nCollectorContext.getMenuId())) {
            return;
        }

        //处理数据，发送给消费端落库
        i18nCollectorContext.setMenuCode(menuHelper.getMenuCodeById(i18nCollectorContext.getMenuId()).orElse(null));

        String message = redisHelper.toJson(i18nCollectorContext);
        if (isRepeated(message, i18nCollectorContext.getType())) {
            return;
        }
        mqClient.output().send(MessageBuilder.withPayload(message).build());
        //LOGGER.debug("发送国际化收集信息,callInfo:{}", message);

    }

    public boolean isRepeated(String text, String type) {
        String mark = assistTextMarker.mark(text);
        return redisHelper.hshHasKey(I18N_CACHE_KEY + type, mark);
    }


}
