package com.firetrot.tools.gct.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firetrot.tools.gct.composer.GdprConsentComposer;
import com.firetrot.tools.gct.handler.ErrorHandler;
import com.firetrot.tools.gct.handler.GdprComposeHandler;
import com.firetrot.tools.gct.handler.GdprParseHandler;
import com.firetrot.tools.gct.parser.GdprConsentParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfiguration {

    @Bean
    public GdprParseHandler gdprParseHandler(GdprConsentParser parser) {
        return new GdprParseHandler(parser);
    }

    @Bean
    public GdprComposeHandler gdprComposeHandler(ObjectMapper objectMapper, GdprConsentComposer gdprConsentComposer) {
        return new GdprComposeHandler(objectMapper, gdprConsentComposer);
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    @Bean
    public RouterFunction<ServerResponse> route(
            GdprComposeHandler gdprComposeHandler,
            GdprParseHandler gdprParseHandler,
            ErrorHandler errorHandler) {

        return RouterFunctions
                .route(RequestPredicates.GET("/api")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), gdprParseHandler)
                .andRoute(RequestPredicates.POST("/api")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)), gdprComposeHandler);
//                .andOther(RouterFunctions.route(RequestPredicates.all(), errorHandler));
    }
}
