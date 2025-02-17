package com.tusdatos.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TusDatosWebClient extends WebClientTemplate {

    public TusDatosWebClient(WebClient webClient) {
        super(webClient);
    }

    @Autowired
    public TusDatosWebClient(final WebClient webClient, ExchangeFilterFunction authentication, @Value("${configuration.tusdatos.url}") String urlBase) {
        this(webClient.mutate().baseUrl(urlBase).filter(authentication).build());
    }

}
