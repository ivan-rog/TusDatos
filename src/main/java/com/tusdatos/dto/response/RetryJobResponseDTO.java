package com.tusdatos.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RetryJobResponseDTO {

    @JsonProperty("doc")
    private String documentNumber;
    private String jobId;
}
