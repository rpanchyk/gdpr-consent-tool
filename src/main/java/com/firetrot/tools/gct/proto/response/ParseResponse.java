package com.firetrot.tools.gct.proto.response;

import com.firetrot.tools.gct.parser.model.GdprConsent;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(staticName = "of")
@Value
public class ParseResponse {

    GdprConsent consent;

    String error;

    public static ParseResponse success(GdprConsent gdprConsent) {
        return ParseResponse.of(gdprConsent, null);
    }

    public static ParseResponse error(String format, Object... args) {
        return ParseResponse.of(null, String.format(format, args));
    }
}
