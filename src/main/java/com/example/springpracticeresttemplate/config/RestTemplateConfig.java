package com.example.springpracticeresttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateConfig {

    @Value("${rest.template-base-url}")
    String baseUrl;

    @Value("${rest.template.username}")
    String username;

    @Value("${rest.template.password}")
    String password;

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {

        assert baseUrl != null;

        var builder = configurer.configure(new RestTemplateBuilder());
        var uriBuilderFactory = new DefaultUriBuilderFactory(baseUrl);

        // use https://github.com/noobdevsam/spring-practice-restmvc/tree/81-security-complete-mvc-tests as rest server
        // and run the server on port 9090 as this application connects to port 9090 for the rest server
        var builderWithAuth = builder.basicAuthentication(username, password);

        return builderWithAuth.uriTemplateHandler(uriBuilderFactory);
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
