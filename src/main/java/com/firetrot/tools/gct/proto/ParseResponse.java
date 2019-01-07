package com.firetrot.tools.gct.proto;

import com.firetrot.tools.gct.parser.model.GdprConsent;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(staticName = "of")
@Value
public class ParseResponse {

    String consent;

    GdprConsent parsedConsent;

    String error;

    public static ParseResponse success(String consent, GdprConsent gdprConsent) {
        return ParseResponse.of(consent, gdprConsent, null);
    }

    public static ParseResponse error(String format, Object... args) {
        return ParseResponse.of(null, null, String.format(format, args));
    }
}
