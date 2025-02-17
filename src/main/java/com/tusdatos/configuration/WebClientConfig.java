package com.tusdatos.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    private ConnectionProvider getConnectionProvider() {
        return ConnectionProvider
                .builder("custom-http-client")
                .maxConnections(50)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(60))
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .evictInBackground(Duration.ofSeconds(120))
                .build();
    }

    private HttpClient createHttpClient() {
        return HttpClient.create(getConnectionProvider())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 40_000)
                .doOnConnected(
                        connection ->
                                connection.addHandlerLast(new ReadTimeoutHandler(40))
                                        .addHandlerLast(new WriteTimeoutHandler(40)));
    }

    @Bean
    public WebClient createWebClient() {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(createHttpClient())).build();
    }

}
