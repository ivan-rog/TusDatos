package com.tusdatos.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportJsonResponseDTO {
    private String rut;

    @JsonProperty("rut_estado")
    private String rutStatus;

    @JsonProperty("nombre")
    private String name;
}
