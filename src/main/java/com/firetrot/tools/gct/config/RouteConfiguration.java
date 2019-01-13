package com.firetrot.tools.gct.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firetrot.tools.gct.composer.GdprConsentComposer;
import com.firetrot.tools.gct.handler.ErrorHandler;
import com.firetrot.tools.gct.handler.GdprComposeHandler;
import com.firetrot.tools.gct.handler.GdprParseHandler;
import com.firetrot.tools.gct.handler.StaticIndexHandler;
import com.firetrot.tools.gct.parser.GdprConsentParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

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
    public StaticIndexHandler staticIndexHandler() {
        return new StaticIndexHandler("static/index.html");
    }

    @Bean
    public Resource staticResource() {
        return new ClassPathResource("static/");
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    @Bean
    public RouterFunction<?> route(
            GdprComposeHandler gdprComposeHandler,
            GdprParseHandler gdprParseHandler,
            StaticIndexHandler staticIndexHandler,
            Resource staticResource,
            ErrorHandler errorHandler) {

        return RouterFunctions
                .route(RequestPredicates.GET("/api")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), gdprParseHandler)
                .andRoute(RequestPredicates.POST("/api")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)), gdprComposeHandler)
                .andRoute(RequestPredicates.GET("/")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), staticIndexHandler)
                .and(RouterFunctions.resources("/**", staticResource))
                .andOther(RouterFunctions.route(RequestPredicates.all(), errorHandler));
    }
}
