package com.example.springpracticeresttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
public class RestTemplateConfig {

    @Value("${rest.template-base-url}")
    String baseUrl;

    @Bean
    RestTemplateBuilder restTemplateBuilder(
            RestTemplateBuilderConfigurer configurer,
            OauthClientInterceptor interceptor
    ) {

        assert baseUrl != null;

        var logbookInteceptor = new LogbookClientHttpRequestInterceptor(
                Logbook.builder().build()
        );

        // use https://github.com/noobdevsam/spring-practice-restmvc/tree/100-logging-add-json-log-output as rest server
        // and run the server on port 8080 or 8081 as this application connects to port 8080 or 8081 for the rest server

        return configurer.configure(new RestTemplateBuilder())
                .additionalInterceptors(interceptor, logbookInteceptor)
                .uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    /**
     * This manager is responsible for handling OAuth2 client credentials and
     * managing authorized client requests in a Spring application.
     * This setup ensures that the application can manage OAuth2 client credentials securely and efficiently.
     */
    @Bean
    OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {

        // An OAuth2AuthorizedClientProvider is created using the OAuth2AuthorizedClientProviderBuilder.
        // The builder is configured to support the clientCredentials grant type.
        // This provider is responsible for obtaining access tokens using the client credentials flow,
        //      which is commonly used for server-to-server communication.
        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService
                );
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }
}
