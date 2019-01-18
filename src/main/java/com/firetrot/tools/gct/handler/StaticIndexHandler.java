package com.firetrot.tools.gct.handler;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class StaticIndexHandler implements HandlerFunction<ServerResponse> {

    private final String path;

    public StaticIndexHandler(String path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_HTML)
                .syncBody(new ClassPathResource(path));
    }
}
