package com.tusdatos.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusdatos.dto.enums.DocumentTypes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaunchResponseDTO {

    @JsonProperty("email")
    private String mail;
    @JsonProperty("doc")
    private String documentNumber;
    @JsonProperty("jobid")
    private String jobId;
    @JsonProperty("nombre")
    private String name;
    @JsonProperty("typedoc")
    private DocumentTypes documentType;
    @JsonProperty("validado")
    private boolean validated;

}
