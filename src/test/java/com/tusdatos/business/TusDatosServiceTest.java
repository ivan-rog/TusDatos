package com.tusdatos.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tusdatos.client.TusDatosWebClient;
import com.tusdatos.dto.request.LaunchRequestDTO;
import com.tusdatos.utils.JacksonUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class TusDatosServiceTest {

    private TusDatosService tusDatosService;
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
        this.tusDatosService = new TusDatosService(this.tusDatosWebClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void when_the_document_type_is_cc() {
        this.mockWebServer.enqueue(this.mockLaunchResponse());
        this.mockWebServer.enqueue(this.mockResultResponse());
        this.tusDatosService.launch(this.mockLaunchRequestCC()).block();
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

    private MockResponse mockLaunchResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("{\n" +
                        "    \"email\": \"usuario@pruebas.com\",\n" +
                        "    \"doc\": 111,\n" +
                        "    \"jobid\": \"6460fc34-4154-43db-9438-8c5a059304c0\",\n" +
                        "    \"nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "    \"typedoc\": \"CC\",\n" +
                        "    \"validado\": true\n" +
                        "}");
    }

    private MockResponse mockResultResponse() {
        return new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("{\n" +
                        "    \"cedula\": 111,\n" +
                        "    \"error\": false,\n" +
                        "    \"errores\": [],\n" +
                        "    \"estado\": \"finalizado\",\n" +
                        "    \"hallazgo\": true,\n" +
                        "    \"hallazgos\": \"Alto\",\n" +
                        "    \"id\": \"651c2ede72476080772781f5\",\n" +
                        "    \"nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "    \"results\": {\n" +
                        "        \"Analisis Reputacional\": true,\n" +
                        "        \"Asociaciones Profesionales\": true,\n" +
                        "        \"CIDOB Peps nivel mundial\": true,\n" +
                        "        \"Concordato de Supersociedades\": true,\n" +
                        "        \"Consejo de Seguridad de la Naciones Unidas (ONU)\": true,\n" +
                        "        \"Contadores Sancionados\": true,\n" +
                        "        \"Contraloría General de la Republica (Consulta en Linea)\": true,\n" +
                        "        \"Contratación Pública en SECOP1 \": true,\n" +
                        "        \"Contratación Pública en SECOP2 \": true,\n" +
                        "        \"DIAN (Proveedores Ficticios)\": true,\n" +
                        "        \"Delitos sexuales contra menores de edad\": true,\n" +
                        "        \"Empresas y Personas Sancionadas Banco Interamericano de Desarrollo (IADB)\": true,\n" +
                        "        \"European Union Most Wanted List (EUROPOL)\": true,\n" +
                        "        \"Fondo de Pensiones Publicas (FOPEP)\": true,\n" +
                        "        \"Histórico de multas en Bogotá (SIMUR)\": true,\n" +
                        "        \"Instituto Nacional Penitenciario y Carcelario (INPEC)\": true,\n" +
                        "        \"Juzgados Tyba - Justicia XXI\": true,\n" +
                        "        \"Libreta Militar\": true,\n" +
                        "        \"Lista Clinton (OFAC), Busqueda por Documento (Consulta en Línea)\": true,\n" +
                        "        \"Lista Clinton (OFAC), Busqueda por Nombre (Consulta en Línea)\": true,\n" +
                        "        \"Listado del Banco Mundial de empresas e individuos no elegibles\": true,\n" +
                        "        \"Listas y PEPs (Personas Expuestas Políticamente), Busqueda por Documento\": true,\n" +
                        "        \"Listas y PEPs (Personas Expuestas Políticamente), Busqueda por Nombre\": true,\n" +
                        "        \"Offshore Leaks Database (ICJI)\": true,\n" +
                        "        \"Organización Internacional de Policía Criminal (INTERPOL)\": true,\n" +
                        "        \"Personeria de Bogota\": true,\n" +
                        "        \"Policia Nacional de Colombia\": true,\n" +
                        "        \"Procuraduría General de la Nación (Consulta en Linea)\": true,\n" +
                        "        \"RGM Registro de Garantías Mobiliarias\": true,\n" +
                        "        \"Rama Judicial Unificada, Busqueda por Nombre\": true,\n" +
                        "        \"Registraduría Nacional del Estado Civil\": true,\n" +
                        "        \"Registro Nacional de Carga (RNDC)\": true,\n" +
                        "        \"Registro Nacional de Medidas Correctivas (RNMC)\": true,\n" +
                        "        \"Registro Único Empresarial y Social (RUES)\": true,\n" +
                        "        \"Registro Único Nacional de Tránsito (RUNT)\": true,\n" +
                        "        \"Registro Único Tributario (RUT)\": true,\n" +
                        "        \"Sancionados contratación pública SECOP\": true,\n" +
                        "        \"Servicio Nacional de Aprendizaje (SENA)\": true,\n" +
                        "        \"Sistema Integrado de Multas y Sanciones de Transito (SIMIT)\": true,\n" +
                        "        \"Sistema de Información de Conductores que Transportan Mercancías Peligrosa (SISCONMP)\": true,\n" +
                        "        \"Sistema de Información del Registro Nacional de Abogados (SIRNA)\": true,\n" +
                        "        \"Sistema de Información y Gestión del Empleo Público (SIGEP)\": true,\n" +
                        "        \"Sistema de Seguridad Social Subsidiado (SISBEN)\": true,\n" +
                        "        \"Vehículos inmovilizados Bogotá\": true\n" +
                        "    },\n" +
                        "    \"time\": 42.67135,\n" +
                        "    \"typedoc\": \"CC\",\n" +
                        "    \"validado\": true\n" +
                        "}");
    }
}