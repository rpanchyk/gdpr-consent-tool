package com.firetrot.tools.gct.config;

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
    public GdprComposeHandler gdprComposeHandler() {
        return new GdprComposeHandler();
    }

    @Bean
    public GdprParseHandler gdprParseHandler(GdprConsentParser parser) {
        return new GdprParseHandler(parser);
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
                .route(RequestPredicates.POST("/api")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)), gdprComposeHandler)
                .andRoute(RequestPredicates.GET("/api")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), gdprParseHandler);
//                .andOther(RouterFunctions.route(RequestPredicates.all(), errorHandler));
    }
}
