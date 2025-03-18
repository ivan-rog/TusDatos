package com.tusdatos.client;

import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
public class WebClientTemplate {

    private final WebClient webClient;

    public WebClientTemplate(final WebClient webClient) {
        this.webClient = webClient;
    }

    protected <T> Mono<T> get(final String uri, final Class<T> responseType) {
        return this.additional(this.webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(responseType)
        );
    }

    protected <T, V> Mono<T> post(final String uri, V body, final Class<T> responseType) {
        return this.additional(this.webClient
                .post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
        );
    }

    public <T> Mono<T> additional(Mono<T> request) {
        return request.transform(mono ->
                mono.retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                                .filter(throwable ->
                                        {
                                            return switch (throwable) {
                                                case ConnectTimeoutException ignored -> true;
                                                case TimeoutException ignored -> true;
                                                case WebClientResponseException webClientResponseException ->
                                                        webClientResponseException.getCause() instanceof TimeoutException
                                                                || webClientResponseException.getStatusCode().is5xxServerError();
                                                case WebClientRequestException webClientRequestException ->
                                                        webClientRequestException.getCause() instanceof TimeoutException;
                                                default -> false;
                                            };
                                        }
                                ))
                        .doOnError(ex -> log.error("Error: ", ex))
        );
}
