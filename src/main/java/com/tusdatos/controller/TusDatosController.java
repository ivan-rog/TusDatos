package com.tusdatos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tusdatos.business.TusDatosService;
import com.tusdatos.dto.request.LaunchRequestDTO;
import com.tusdatos.utils.JacksonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class TusDatosController {

    private final TusDatosService tusDatosService;

    public TusDatosController(TusDatosService tusDatosService) {
        this.tusDatosService = tusDatosService;
    }

    @GetMapping("/launch")
    public String lauch() throws JsonProcessingException {
        this.tusDatosService.launch(JacksonUtils.jsonToObject("{ \"doc\": 111, \"typedoc\": \"CC\", \"fechaE\": \"01/12/2014\" }", LaunchRequestDTO.class)).subscribe(jobStatusResponseDTO -> System.out.println(jobStatusResponseDTO));
        return "Launch Tus";
    }
}
