package com.tusdatos.business;

import com.tusdatos.client.TusDatosWebClient;
import com.tusdatos.dto.request.LaunchRequestDTO;
import com.tusdatos.dto.response.JobStatusResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TusDatosService {

    private final TusDatosWebClient tusDatosWebClient;

    public TusDatosService(final TusDatosWebClient tusDatosWebClient) {
        this.tusDatosWebClient = tusDatosWebClient;
    }

    public Mono<JobStatusResponseDTO> launch(final LaunchRequestDTO launchRequestDTO) {
        return tusDatosWebClient.launch(launchRequestDTO).
                flatMap(this.tusDatosWebClient::jobStatus).
                flatMap(this.tusDatosWebClient::retryJob).log();
    }

}
