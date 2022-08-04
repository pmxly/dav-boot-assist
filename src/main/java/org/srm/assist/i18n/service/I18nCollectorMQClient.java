package org.srm.assist.i18n.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface I18nCollectorMQClient {
    @Output("SAST_I18N_COLLECTOR_TOPIC")
    MessageChannel output();
}
