package com.tusdatos.client;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientTemplate {

    private final WebClient webClient;

    public WebClientTemplate (final WebClient webClient) {
        this.webClient = webClient;
    }

    protected <T> Mono<T> get(final String uri, final Class<T> responseType) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(responseType);
    }

    protected <T, V> Mono<T> post(final String uri, V body, final Class<T> responseType) {
        return webClient
                .post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }
}
