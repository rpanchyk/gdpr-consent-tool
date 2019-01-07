package com.firetrot.tools.gct.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firetrot.tools.gct.composer.GdprConsentComposer;
import com.firetrot.tools.gct.exception.BadRequestException;
import com.firetrot.tools.gct.proto.request.ComposeRequest;
import com.firetrot.tools.gct.proto.response.ComposeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;

public class GdprComposeHandler implements HandlerFunction<ServerResponse> {

    private final ObjectMapper objectMapper;
    private final GdprConsentComposer gdprConsentComposer;

    public GdprComposeHandler(ObjectMapper objectMapper, GdprConsentComposer gdprConsentComposer) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.gdprConsentComposer = Objects.requireNonNull(gdprConsentComposer);
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.body(BodyExtractors.toMono(String.class))
                .flatMap(this::toComposeRequest)
                .flatMap(this::composeConsent)
                .flatMap(GdprComposeHandler::successResponse)
                .onErrorResume(GdprComposeHandler::errorResponse);
    }

    private Mono<ComposeRequest> toComposeRequest(String value) {
        try {
            return Mono.just(objectMapper.readValue(value, ComposeRequest.class));
        } catch (IOException e) {
            return Mono.error(new BadRequestException(
                    String.format("Cannot encode %s, error: %s", value, e.getMessage()), e));
        }
    }

    private Mono<String> composeConsent(ComposeRequest composeRequest) {
        return gdprConsentComposer.compose(composeRequest);
    }

    private static Mono<ServerResponse> successResponse(String consent) {
        return respondWith(HttpStatus.OK, ComposeResponse.success(consent));
    }

    private static Mono<ServerResponse> errorResponse(Throwable throwable) {
        final HttpStatus status;
        final String message;

        if (throwable instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Invalid request";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Cannot compose consent";
        }

        return respondWith(status, ComposeResponse.error(message + ", error: %s", throwable.getMessage()));
    }

    private static Mono<ServerResponse> respondWith(HttpStatus status, ComposeResponse response) {
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(response));
    }
}
