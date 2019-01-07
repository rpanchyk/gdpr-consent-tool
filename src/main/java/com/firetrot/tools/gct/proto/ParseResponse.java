package com.firetrot.tools.gct.proto;

import com.firetrot.tools.gct.parser.model.GdprConsent;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(staticName = "of")
@Value
public class ParseResponse {

    GdprConsent result;

    String error;

    public static ParseResponse success(GdprConsent result) {
        return ParseResponse.of(result, null);
    }

    public static ParseResponse error(String format, Object... args) {
        return ParseResponse.of(null, String.format(format, args));
    }
}
