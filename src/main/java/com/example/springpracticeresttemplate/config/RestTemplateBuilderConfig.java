package com.example.springpracticeresttemplate.config;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
        var builder = configurer.configure(new RestTemplateBuilder());
        var uriBuilderFactory = new DefaultUriBuilderFactory("http://localhost:9090");
        return builder.uriTemplateHandler(uriBuilderFactory);
    }
}
