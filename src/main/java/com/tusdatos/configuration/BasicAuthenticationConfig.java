package com.tusdatos.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@Configuration
public class BasicAuthenticationConfig {

    @Bean
    public ExchangeFilterFunction basicAuth(
            @Value("${configuration.tusdatos.user}") String user,
            @Value("${configuration.tusdatos.password}") String password
    ) {
        return basicAuthentication(user, password);
    }
}
