package com.tusdatos.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusdatos.dto.enums.DocumentTypes;
import lombok.*;

@Setter
@Getter
@ToString
public class LaunchRequestDTO {

    @JsonProperty("doc")
    @JsonAlias("doc")
    private String documentNumber;
    @JsonProperty("typedoc")
    @JsonAlias("typedoc")
    private DocumentTypes documentTypes;
    @JsonProperty("fechaE")
    @JsonAlias("fechaE")
    private String expirationDate;

}
