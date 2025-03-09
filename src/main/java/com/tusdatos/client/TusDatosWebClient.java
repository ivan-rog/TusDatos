package com.tusdatos.client;

import com.tusdatos.dto.request.LaunchRequestDTO;
import com.tusdatos.dto.response.JobStatusResponseDTO;
import com.tusdatos.dto.response.LaunchResponseDTO;
import com.tusdatos.dto.response.ReportJsonResponseDTO;
import com.tusdatos.dto.response.RetryJobResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class TusDatosWebClient extends WebClientTemplate {

    @Value("${configuration.tusdatos.endpoint.launch}")
    private String endpointLaunch;

    @Value("${configuration.tusdatos.endpoint.job.status}")
    private String endpointJobStatus;

    @Value("${configuration.tusdatos.endpoint.job.retry}")
    private String endpointJobRetry;

    @Value("${configuration.tusdatos.endpoint.report.json}")
    private String endpointReportJson;

    private final List<String> sources = List.of("interpol");

    public TusDatosWebClient(WebClient webClient) {
        super(webClient);
    }

    @Autowired
    public TusDatosWebClient(final WebClient webClient, ExchangeFilterFunction authentication, @Value("${configuration.tusdatos.url}") String urlBase) {
        this(webClient.mutate().baseUrl(urlBase).filter(authentication).build());
    }

    public Mono<ReportJsonResponseDTO> processDocuments(final LaunchRequestDTO launchRequestDTO) {
        return this.launch(launchRequestDTO).
                flatMap(this::jobStatus).
                flatMap(this::retryJob).
                flatMap(this::reportJob);
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

    public Mono<JobStatusResponseDTO> jobStatus(final RetryJobResponseDTO retryJobResponseDTO) {
        var uri = UriComponentsBuilder.
                fromUriString(this.endpointJobStatus).
                encode().buildAndExpand(retryJobResponseDTO.getJobId()).toUri();
        return Flux.interval(Duration.ofSeconds(15)).
                flatMap(i -> this.get(uri.toString(), JobStatusResponseDTO.class)).
                takeUntil(jobStatusResponseDTO -> "finalizado".equals(jobStatusResponseDTO.getStatus())).
                last().timeout(Duration.ofMinutes(2));
    }

    public Mono<JobStatusResponseDTO> retryJob(final JobStatusResponseDTO jobStatusResponseDTO) {
        if(jobStatusResponseDTO.isError() && validateSource(jobStatusResponseDTO.getErrors())) {
            var uri =  UriComponentsBuilder.fromUriString(this.endpointJobRetry).encode().
                    buildAndExpand(jobStatusResponseDTO.getId(), jobStatusResponseDTO.getCedula()).toUri();
            return Flux.range(0, 3).
                    flatMap( attempt -> this.get(uri.toString(), RetryJobResponseDTO.class)).
                    flatMap(this::jobStatus).
                    takeUntil(
                            jobStatusResponseDTORetry -> jobStatusResponseDTO.isError() && validateSource(jobStatusResponseDTORetry.getErrors())
                    ).last();
        }
        return Mono.just(jobStatusResponseDTO);
    }

    public Mono<ReportJsonResponseDTO> reportJob(final JobStatusResponseDTO jobStatusResponseDTO) {
        var uri = UriComponentsBuilder.fromUriString(this.endpointReportJson)
                .encode()
                .buildAndExpand(jobStatusResponseDTO.getId())
                .toUri();
        return this.get(uri.toString(), ReportJsonResponseDTO.class);
    }

    public boolean validateSource(List<String> errors){
        return errors.stream().anyMatch(this.sources::contains);
    }

}
