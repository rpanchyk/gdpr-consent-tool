package com.firetrot.tools.gct.proto.response;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(staticName = "of")
@Value
public class ComposeResponse {

    String consent;

    String error;

    public static ComposeResponse success(String consent) {
        return ComposeResponse.of(consent, null);
    }

    public static ComposeResponse error(String format, Object... args) {
        return ComposeResponse.of(null, String.format(format, args));
    }
}
