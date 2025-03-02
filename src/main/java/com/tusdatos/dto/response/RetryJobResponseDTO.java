package com.tusdatos.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetryJobResponseDTO {

    @JsonProperty("doc")
    private String documentNumber;
    private String jobId;
}
