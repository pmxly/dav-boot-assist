package org.srm.assist.common.marker;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class Md5BasedAssistTextMarker implements AssistTextMarker {
    @Override
    public String mark(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }
}
