package com.reliaquest.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class RestClientConfig {

    @Value("${mockserver.baseurl}")
    private String baseUrl;

    @Bean
    public RestClient restClient() {
        log.info("Creating RestClient bean...");
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
