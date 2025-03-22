package com.tusdatos.business;

import com.tusdatos.client.TusDatosWebClient;
import com.tusdatos.dto.request.LaunchRequestDTO;
import com.tusdatos.dto.response.ReportJsonResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TusDatosService {

    private final TusDatosWebClient tusDatosWebClient;

    public TusDatosService(final TusDatosWebClient tusDatosWebClient) {
        this.tusDatosWebClient = tusDatosWebClient;
    }

    public void launch(final LaunchRequestDTO launchRequestDTO) {
        this.tusDatosWebClient.processDocuments(launchRequestDTO).
                subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(ReportJsonResponseDTO reportJsonResponseDTO) {
        log.info("Respuesta TUS DATOS: {}", reportJsonResponseDTO.toString());
    }

    private void onError(Throwable throwable) {
        log.error("Error TUS_DATOS: {}", throwable.getMessage(), throwable);
    }

}
