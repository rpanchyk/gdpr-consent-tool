package com.firetrot.tools.gct.config;

import com.firetrot.tools.gct.composer.GdprConsentComposer;
import com.firetrot.tools.gct.parser.GdprConsentParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public GdprConsentParser gdprConsentParser() {
        return new GdprConsentParser();
    }

    @Bean
    public GdprConsentComposer gdprConsentComposer() {
        return new GdprConsentComposer();
    }
}
