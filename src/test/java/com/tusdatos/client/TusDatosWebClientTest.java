package com.tusdatos.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tusdatos.dto.request.LaunchRequestDTO;
import com.tusdatos.utils.JacksonUtils;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TusDatosWebClientTest {

    private TusDatosWebClient tusDatosWebClient;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        this.tusDatosWebClient = new TusDatosWebClient(WebClient.create(this.mockWebServer.url("/").toString()));
        ReflectionTestUtils.setField(this.tusDatosWebClient, "endpointLaunch", "/api/launch");
        ReflectionTestUtils.setField(this.tusDatosWebClient, "endpointJobStatus", "/api/results/{jobkey}");
        ReflectionTestUtils.setField(this.tusDatosWebClient, "endpointJobRetry", "/api/retry/{id}?typedoc={typedoc}");
    }

    @AfterEach
    void tearDown() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void when_the_document_type_is_cc() {
        this.tusDatosWebClient.launch(this.mockLaunchRequestCC()).block();
    }

    private LaunchRequestDTO mockLaunchRequestCC() {
        try {
            return JacksonUtils.jsonToObject(
                    "{ \"doc\": 111, \"typedoc\": \"CC\", \"fechaE\": \"01/12/2014\" }",
                    LaunchRequestDTO.class
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}