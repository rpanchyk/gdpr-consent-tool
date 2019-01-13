package com.firetrot.tools.gct.handler;

import com.firetrot.tools.gct.exception.BadRequestException;
import com.firetrot.tools.gct.parser.GdprConsentParser;
import com.firetrot.tools.gct.parser.model.GdprConsent;
import com.firetrot.tools.gct.proto.response.ParseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class GdprParseHandler implements HandlerFunction<ServerResponse> {

    private static final String CONSENT_PARAM = "consent";

    private final GdprConsentParser parser;

    public GdprParseHandler(GdprConsentParser parser) {
        this.parser = Objects.requireNonNull(parser);
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.queryParam(CONSENT_PARAM)
                .filter(consent -> !consent.isEmpty())
                .map(this::processResponse)
                .orElseGet(() -> errorResponse(new BadRequestException("No 'consent' param given"), null));
    }

    private Mono<ServerResponse> processResponse(String consent) {
        return parser.parse(consent)
                .flatMap(gdprConsent -> successResponse(gdprConsent, consent))
                .onErrorResume(throwable -> errorResponse(throwable, consent));
    }

    private Mono<ServerResponse> successResponse(GdprConsent gdprConsent, String consent) {
        return respondWith(HttpStatus.OK, ParseResponse.success(gdprConsent));
    }

    private Mono<ServerResponse> errorResponse(Throwable throwable, String consent) {
        final HttpStatus status;
        final String message;

        if (throwable instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Invalid consent given";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Cannot parse consent";
        }

        return respondWith(status, ParseResponse.error(message + " '%s', error: %s", consent, throwable.getMessage()));
    }

    private static Mono<ServerResponse> respondWith(HttpStatus status, ParseResponse response) {
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(response));
    }
}
