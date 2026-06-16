package com.mini.commerce.kampus.aryo.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import lombok.Getter;

@Configuration
public class AppConfig {
    @Value("${service.catalog.url}")
    private String catalogServiceUrl;

    @Bean
    public RestClient catalogRestClient() {
        return RestClient.builder()
                .baseUrl(catalogServiceUrl)
                .build();
    }
}
