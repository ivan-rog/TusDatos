package com.tusdatos.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusdatos.dto.enums.DocumentTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class JobStatusResponseDTO {

    private String cedula;
    private boolean error;
    @JsonProperty("errores")
    private List<String> errors;
    @JsonProperty("estado")
    private String status;
    @JsonProperty("hallazgo")
    private boolean finding;
    @JsonProperty("hallazgos")
    private String findings;
    private String id;
    @JsonProperty("nombre")
    private String name;
    private Object result;
    private long time;
    @JsonProperty("typedoc")
    private DocumentTypes documentTypes;
    @JsonProperty("validado")
    private boolean validated;

}
