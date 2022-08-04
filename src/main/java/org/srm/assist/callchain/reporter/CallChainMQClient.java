package org.srm.assist.callchain.reporter;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CallChainMQClient {
    @Output()
    MessageChannel output();
}
