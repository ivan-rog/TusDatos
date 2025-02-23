package com.tusdatos.client;

import com.tusdatos.dto.request.LaunchRequestDTO;
import com.tusdatos.dto.response.JobStatusResponseDTO;
import com.tusdatos.dto.response.LaunchResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class TusDatosWebClient extends WebClientTemplate {

    @Value("${configuration.tusdatos.endpoint.launch}")
    private String endpointLaunch;

    @Value("${configuration.tusdatos.endpoint.job.status}")
    private String endpointJobStatus;

    public TusDatosWebClient(WebClient webClient) {
        super(webClient);
    }

    @Autowired
    public TusDatosWebClient(final WebClient webClient, ExchangeFilterFunction authentication, @Value("${configuration.tusdatos.url}") String urlBase) {
        this(webClient.mutate().baseUrl(urlBase).filter(authentication).build());
    }

    public Mono<LaunchResponseDTO> launch(final LaunchRequestDTO launchRequestDTO) {
        return this.post(this.endpointLaunch, launchRequestDTO, LaunchResponseDTO.class);
    }

    public Mono<JobStatusResponseDTO> jobStatus(final LaunchResponseDTO launchResponseDTO) {
        var uri = UriComponentsBuilder.
                fromUriString(this.endpointJobStatus).
                encode().buildAndExpand(launchResponseDTO.getJobId()).toUri();
        return Flux.interval(Duration.ofSeconds(15)).
                flatMap(i -> this.get(uri.toString(), JobStatusResponseDTO.class)).
                takeUntil(jobStatusResponseDTO -> "finalizado".equals(jobStatusResponseDTO.getStatus())).
                last().timeout(Duration.ofMinutes(2));
    }

}
