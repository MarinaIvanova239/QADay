package com.dins.integration.common.config;

import com.dins.services.RestClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class IntegrationContext {

    @Bean
    RestClient restClient() {
        return new RestClient();
    }
}
