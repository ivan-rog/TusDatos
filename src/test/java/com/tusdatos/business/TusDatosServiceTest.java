package com.tusdatos.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tusdatos.client.TusDatosWebClient;
import com.tusdatos.dto.request.LaunchRequestDTO;
import com.tusdatos.dto.response.JobStatusResponseDTO;
import com.tusdatos.dto.response.LaunchResponseDTO;
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
import reactor.test.StepVerifier;

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
    void when_the_document_type_is_cc() throws JsonProcessingException {
        this.mockWebServer.enqueue(this.mockLaunchResponse());
        this.mockWebServer.enqueue(this.mockResultResponse());
        this.mockWebServer.enqueue(this.mockReportJson());
    }

    private MockResponse mockReportJson() {
        return new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("{\n" +
                        "    \"cidob\": {\n" +
                        "        \"Alias\": \" \",\n" +
                        "        \"Cargo\": \"Primer ministro (2010-2014)\",\n" +
                        "        \"Informacion_completa\": \"link\",\n" +
                        "        \"Mandato\": \"23 mayo 2013 - 7 abril 2016\",\n" +
                        "        \"Nacimiento\": \"Ciudad prueba,  marzo 1975\",\n" +
                        "        \"Pais\": \"Pais pruebas\",\n" +
                        "        \"Partido político\": \"Partido de pruebas (PD)\"\n" +
                        "    },\n" +
                        "    \"contadores_s\": [\n" +
                        "        {\n" +
                        "            \"c_dula\": \"111\",\n" +
                        "            \"contador\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"fecha_ejecutoria\": \"2010-06-23T00:00:00.000\",\n" +
                        "            \"fecha_fin\": \"2010-12-23T00:00:00.000\",\n" +
                        "            \"fecha_inicio\": \"2010-06-23T00:00:00.000\",\n" +
                        "            \"fecha_registro\": \"2010-07-14T00:00:00.000\",\n" +
                        "            \"fecha_resoluci_n\": \"2009-12-03T00:00:00.000\",\n" +
                        "            \"meses\": \"6\",\n" +
                        "            \"proceso_jur_dico\": \"1235\",\n" +
                        "            \"resoluci_n\": \"372\",\n" +
                        "            \"tipo\": \"SUSPENSIÓN\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"contaduria\": true,\n" +
                        "    \"contaduria_pdf\": true,\n" +
                        "    \"contraloria\": true,\n" +
                        "    \"contraloria2\": true,\n" +
                        "    \"dest\": \"static/img/63776b99-4f26-4f11-b426-dcbbb124dfb8\",\n" +
                        "    \"dict_hallazgos\": {\n" +
                        "        \"altos\": [\n" +
                        "            {\n" +
                        "                \"codigo\": \"sirna\",\n" +
                        "                \"descripcion\": \"El titular del documento consultado aparece con sanciones vigentes en los archivos de Antecedentes Disciplinarios de la Comisión, así como los del Tribunal Disciplinario y los de la Sala Jurisdiccional Disciplinaria en el ejercicio de su profesión como abogado.\",\n" +
                        "                \"fuente\": \"sirna\",\n" +
                        "                \"hallazgo\": \"SIRNA: Esta sancionado en el Sistema de Información del Registro Nacional de Abogados\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"bajos\": [\n" +
                        "            {\n" +
                        "                \"codigo\": \"rama_demandante\",\n" +
                        "                \"descripcion\": \"El titular consultado ha presentado una demanda contra una persona o entidad, pero la información proporcionada no es suficiente para identificar claramente el tipo de proceso judicial en el que está involucrado el demandado en la Rama Judicial Unificada. Te sugerimos revisar y analizar el detalle de la consulta asociados con el proceso para que puedas identificar y entender mejor la naturaleza del caso.\",\n" +
                        "                \"fuente\": \"rama_unificada\",\n" +
                        "                \"hallazgo\": \"Rama Unificada: Registra como demandante en un proceso no identificado (Estudiar evidencia)\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"codigo\": \"secop2\",\n" +
                        "                \"descripcion\": \"El documento registra en la base de datos SECOP 2 (Sistema Electrónico de Contratación Pública), dentro de la cual se encuentran los datos de procesos de contratación con el estado registrados en Colombia compra eficiente.\",\n" +
                        "                \"fuente\": \"secop2\",\n" +
                        "                \"hallazgo\": \"SECOP 2: Registra en la fuente\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"codigo\": \"reputacional_news\",\n" +
                        "                \"descripcion\": \"Es posible que el titular de documento se encuentra en al menos una noticia o sitio de internet al consultarlo con base a su nombre completo. Las noticias pueden tener un sentimiento positivo, neutral o negativo. Para mayor información revisar el enlace de la noticia en la sección reputacional.  \",\n" +
                        "                \"fuente\": \"reputacional\",\n" +
                        "                \"hallazgo\": \"Noticias Reputacionales\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"codigo\": \"reputacional_social\",\n" +
                        "                \"descripcion\": \"Es posible que el titular de documento se encuentra en alguna red social como Facebook, LinkedIn, YouTube o Twitter. Para mayor información revisar el enlace de la red social en la sección reputacional. \",\n" +
                        "                \"fuente\": \"reputacional\",\n" +
                        "                \"hallazgo\": \"Redes Sociales\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"infos\": [\n" +
                        "            {\n" +
                        "                \"codigo\": \"rut\",\n" +
                        "                \"descripcion\": \"El titular del documento está inscrito en el Registro Único Tributario (RUT).\",\n" +
                        "                \"fuente\": \"rut\",\n" +
                        "                \"hallazgo\": \"RUT: Inscrito en Registro Único Tributario\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"codigo\": \"libretamilitar_info\",\n" +
                        "                \"descripcion\": \"Información de la Libreta Militar para personas que tienen su situación definida expedido por el Comando de Reclutamiento y Control de Reservas del Ejército de Colombia.\",\n" +
                        "                \"fuente\": \"libretamilitar\",\n" +
                        "                \"hallazgo\": \"Libreta Militar: Titular posee libreta militar: Reservista-2da Clase\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"medios\": [\n" +
                        "            {\n" +
                        "                \"codigo\": \"juzgados_tyba\",\n" +
                        "                \"descripcion\": \"El titular consultado se encuentra relacionado ya sea como demandado o demandante, pero la información proporcionada no es suficiente para identificar claramente el tipo de proceso judicial en el que está involucrado en los Juzgados TYBA Te sugerimos revisar y analizar el detalle de la consulta asociados con el proceso para que puedas identificar y entender mejor la naturaleza del caso.\",\n" +
                        "                \"fuente\": \"juzgados_tyba\",\n" +
                        "                \"hallazgo\": \"Juzgados TYBA: Registra en la Rama Judicial TYBA\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"error\": false,\n" +
                        "    \"errores\": [],\n" +
                        "    \"europol\": {\n" +
                        "        \"birth_date\": \"Ene 22, 1975 (48 años)\",\n" +
                        "        \"crimes\": [\n" +
                        "            \"crimen1\",\n" +
                        "            \"crimen2\"\n" +
                        "        ],\n" +
                        "        \"gender\": \"Hombre\",\n" +
                        "        \"more_info\": \"Info\",\n" +
                        "        \"name\": \"PEREZ GOMEZ, Miguel Fernando\",\n" +
                        "        \"nationality\": \"Prueba\",\n" +
                        "        \"picture\": \"link\",\n" +
                        "        \"state_of_case\": \"Investigación activa\",\n" +
                        "        \"status\": \"Wanted by prueba\"\n" +
                        "    },\n" +
                        "    \"fecha\": \"Este reporte fue generado el 4, Oct 2023 a las 10:00AM\",\n" +
                        "    \"fopep\": {\n" +
                        "        \"documento\": \"111\",\n" +
                        "        \"estado\": \"\",\n" +
                        "        \"fecha_inclusion\": \"2014-12-01\",\n" +
                        "        \"tipo_documento\": \"CC\"\n" +
                        "    },\n" +
                        "    \"garantias_mobiliarias\": [\n" +
                        "        {\n" +
                        "            \"Acreedor(es)\": \"Banco s.a.\",\n" +
                        "            \"Detalles\": {\n" +
                        "                \"Acreedor\": {\n" +
                        "                    \"Celular\": \"\",\n" +
                        "                    \"Ciudad\": \"Ciudad de prueba\",\n" +
                        "                    \"Correo Electrónico 1\": \"\",\n" +
                        "                    \"Correo Electrónico 2\": \"\",\n" +
                        "                    \"Dirección\": \"\",\n" +
                        "                    \"Dígito De Verificación\": \"8\",\n" +
                        "                    \"Número de Identificación\": \"852\",\n" +
                        "                    \"Porcentaje de participación\": \"100,00%\",\n" +
                        "                    \"Razón Social o Nombre\": \"Banco s.a.\",\n" +
                        "                    \"Teléfono\": \"\",\n" +
                        "                    \"Tipo Identificación\": \"NIT\"\n" +
                        "                },\n" +
                        "                \"Bienes garantizados\": {},\n" +
                        "                \"Bienes inmuebles\": {},\n" +
                        "                \"Deudor\": {\n" +
                        "                    \"Bien para uso\": \"\",\n" +
                        "                    \"Celular\": \"\",\n" +
                        "                    \"Ciudad\": \"PASTO\",\n" +
                        "                    \"Correo Electrónico\": \"\",\n" +
                        "                    \"Dirección\": \"\",\n" +
                        "                    \"Dígito De Verificación\": \"\",\n" +
                        "                    \"Género\": \"MASCULINO\",\n" +
                        "                    \"Número de Identificación\": \"111\",\n" +
                        "                    \"Razón Social o Nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "                    \"Sectores\": \"Otras actividades de servicios.\",\n" +
                        "                    \"Tamaño de la empresa\": \"\",\n" +
                        "                    \"Teléfono\": \"\",\n" +
                        "                    \"Tipo Identificación\": \"CEDULA DE CIUDADANIA\"\n" +
                        "                },\n" +
                        "                \"Info general\": {\n" +
                        "                    \"Fecha Finalización\": \"19/01/2030 11:59:59 p.m.\",\n" +
                        "                    \"Fecha de inscripción en el registro especial o de celebración del contrato\": \"\",\n" +
                        "                    \"Garantía Inscrita en un Registro Especial\": \"\",\n" +
                        "                    \"Monto Máximo de la obligación garantizada\": \"$ 69.990.000\",\n" +
                        "                    \"Tipo Garantía\": \"Garantía Mobiliaria\"\n" +
                        "                }\n" +
                        "            },\n" +
                        "            \"Fecha de inscripción inicial\\n(dd/mm/aaaa hh:mm:ss)\": \"23/01/2018 12:49:25 p.m.\",\n" +
                        "            \"Folio Electrónico\": \"45484864564211351\",\n" +
                        "            \"Garante - Deudor\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"Número de Identificación\": \"111\",\n" +
                        "            \"Ultima Operación\": \"Formulario Registral de Inscripción Inicial\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"genero\": \"M\",\n" +
                        "    \"google\": [\n" +
                        "        {\n" +
                        "            \"description\": \"Descripción de prueba\",\n" +
                        "            \"idioma\": \"es\",\n" +
                        "            \"keyword\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"link\": \"link\",\n" +
                        "            \"rank\": 1,\n" +
                        "            \"sentimiento\": \"neutral\",\n" +
                        "            \"source\": \"google\",\n" +
                        "            \"title\": \"MIGUEL FERNANDO PEREZ GOMEZ\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"description\": \"Descripccion de prueba 2\",\n" +
                        "            \"idioma\": \"es\",\n" +
                        "            \"keyword\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"link\": \"link\",\n" +
                        "            \"rank\": 3,\n" +
                        "            \"sentimiento\": \"neutral\",\n" +
                        "            \"source\": \"google\",\n" +
                        "            \"title\": \"titulo de prueba\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"hallazgos\": \"alto\",\n" +
                        "    \"iadb\": [\n" +
                        "        {\n" +
                        "            \"_from\": \"January 10, 2022\",\n" +
                        "            \"country\": \"PAIS DE PRUEBA\",\n" +
                        "            \"entity\": \"Individual\",\n" +
                        "            \"grounds\": \"Fraud\",\n" +
                        "            \"nationality\": \"NACIONALIDAD DE PRUEBA\",\n" +
                        "            \"source\": \"IDB\",\n" +
                        "            \"title\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"to\": \"January 09, 2025\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"id\": \"19272354\",\n" +
                        "    \"inmov_bog\": {\n" +
                        "        \"datos_de_entrada_a_patios\": [\n" +
                        "            {\n" +
                        "                \"clase_vehículo\": \"10 - MOTOCICLETA\",\n" +
                        "                \"comparendo\": \" 192545512\",\n" +
                        "                \"conductor\": \"\",\n" +
                        "                \"dirección\": \"CARRERA 72 - CALLE 14 \",\n" +
                        "                \"fecha_de_entrada\": \"06/15/2012 10:52\",\n" +
                        "                \"fecha_de_inmovilización\": \"06/15/2012 10:52\",\n" +
                        "                \"imagen_comparendo\": \"VER COMPARENDO\",\n" +
                        "                \"infracción\": \"DESCRIPCIÓN DE LA INFRACCIÓN\",\n" +
                        "                \"nombre_conductor\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "                \"nro_documento\": \"111\",\n" +
                        "                \"nro_inventario\": \"816217\",\n" +
                        "                \"patio\": \"NUMERO PATIO\",\n" +
                        "                \"placa\": \"BKA452\",\n" +
                        "                \"servicio\": \"OFICIAL\",\n" +
                        "                \"tipo_documento\": \"CEDULA DE CIUDADANIA\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"datos_de_salida_a_patios\": [\n" +
                        "            {\n" +
                        "                \"autorizado_salida\": \"\",\n" +
                        "                \"día_autorización\": \"06/16/2012 10:57\",\n" +
                        "                \"el_vehículo_debe_salir_en_grúa\": \"NO\",\n" +
                        "                \"fecha_salida\": \"SIN INFORMACION\",\n" +
                        "                \"nombre_autorizado\": \"FERNANDOROJAS CARPETA \",\n" +
                        "                \"nro_autorización\": \"11238\",\n" +
                        "                \"nro_documento\": \"111\",\n" +
                        "                \"nro_factura\": \"SIN INFORMACION\",\n" +
                        "                \"tipo_autorización\": \"DEFINITIVA \",\n" +
                        "                \"tipo_documento\": \"CEDULA DE CIUDADANIA\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"inpec\": {\n" +
                        "        \"Establecimiento a cargo\": \"COMPLEJO CARCELARIO Y PENITENCIARIO CIUDAD DE PRUEBA\",\n" +
                        "        \"Estado de ingreso\": \"PRISION DOMICILIARIA\",\n" +
                        "        \"Género\": \"MASCULINO\",\n" +
                        "        \"Identificación\": \"111\",\n" +
                        "        \"Nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "        \"Número único (INPEC)\": \"486524\",\n" +
                        "        \"Situación jurídica\": \"CONDENADO\"\n" +
                        "    },\n" +
                        "    \"interpol\": true,\n" +
                        "    \"juzgados_tyba\": [\n" +
                        "        {\n" +
                        "            \"CODIGO PROCESO\": \"110541468445212351854\",\n" +
                        "            \"DESPACHO\": \"SALA PLENA CONSEJO DE PRUEBA\",\n" +
                        "            \"INFO PROCES0\": {\n" +
                        "                \"Celular\": \"\",\n" +
                        "                \"Ciudad\": \"PRUEBA, D.C\",\n" +
                        "                \"Clase Proceso\": \"PERDIDA DE INVESTIDURA\",\n" +
                        "                \"Corporación\": \"CONSEJO DE PRUEBA\",\n" +
                        "                \"Correo Electrónico Externo\": \"prueba@consejodeprueba.gov.co\",\n" +
                        "                \"Código Proceso\": \"110541468445212351854\",\n" +
                        "                \"Departamento\": \"DEPARTAMENTO DE PRUEBA\",\n" +
                        "                \"Despacho\": \"sala plena consejo de prueba\",\n" +
                        "                \"Dirección\": \"\",\n" +
                        "                \"Distrito\\\\Circuito\": \"ÚNICO NACIONAL\",\n" +
                        "                \"Especialidad\": \"CONSEJO DE PRUEBA - SALA PLENA\",\n" +
                        "                \"Fecha Finalización\": \"\",\n" +
                        "                \"Fecha Providencia\": \"16/08/2018\",\n" +
                        "                \"Fecha Publicación\": \"28/08/2018\",\n" +
                        "                \"Número Despacho\": \"000\",\n" +
                        "                \"Observaciones Finalización\": \"\",\n" +
                        "                \"Subclase Proceso\": \"EN GENERAL / SIN SUBCLASE\",\n" +
                        "                \"Teléfono\": \"\",\n" +
                        "                \"Tipo Decisión\": \"ADMITE\",\n" +
                        "                \"Tipo Proceso\": \"CONSTITUCIONAL\",\n" +
                        "                \"actuaciones\": [],\n" +
                        "                \"sujetos\": [\n" +
                        "                    {\n" +
                        "                        \"ES EMPLAZADO\": \"NO\",\n" +
                        "                        \"FECHA REGISTRO\": \"28-08-2018\",\n" +
                        "                        \"NOMBRE(S) Y APELLIDO(S) / RAZÓN SOCIAL\": \"DEMANDANTE DE PRUEBA\",\n" +
                        "                        \"NÚMERO DE IDENTIFICACIÓN\": \"1.485.123.584\",\n" +
                        "                        \"TIPO DOCUMENTO\": \"CC\",\n" +
                        "                        \"TIPO SUJETO\": \"DEMANDANTE/ACCIONANTE\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"libretamilitar\": {\n" +
                        "        \"clase\": \"Reservista-1ra Clase\",\n" +
                        "        \"documento\": \"111\",\n" +
                        "        \"nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "        \"tipo_documento\": \"Cédula de Ciudadanía\"\n" +
                        "    },\n" +
                        "    \"lista_banco_mundial\": {\n" +
                        "        \"debarred_firms_individuals\": [],\n" +
                        "        \"others_sanctions\": []\n" +
                        "    },\n" +
                        "    \"lista_onu\": true,\n" +
                        "    \"monitoring_date\": \"Wed, 27 Sep 2023 00:00:00 GMT\",\n" +
                        "    \"nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "    \"nombre-procuraduria\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "    \"ofac\": {\n" +
                        "        \"\": \"Ciudad de prueba, Pais de prueba\",\n" +
                        "        \"Citizenship:\": \"\",\n" +
                        "        \"Date of Birth:\": \"22 Jen 1975\",\n" +
                        "        \"First Name:\": \"Miguel Fernando\",\n" +
                        "        \"Last Name:\": \"Perez Gomez\",\n" +
                        "        \"List:\": \"Individual\",\n" +
                        "        \"Nationality:\": \"Nacionalidad de prueba\",\n" +
                        "        \"Place of Birth:\": \"Ciudad de prueba, Pais de prueba\",\n" +
                        "        \"Program:\": \"REALES BRITTO\",\n" +
                        "        \"Remarks:\": \"\",\n" +
                        "        \"Title:\": \"\",\n" +
                        "        \"Type:\": \"Individual\",\n" +
                        "        \"addresses\": [\n" +
                        "            {\n" +
                        "                \"Address\": \"\",\n" +
                        "                \"City\": \"\",\n" +
                        "                \"Country\": \"Pais de prueba\",\n" +
                        "                \"Postal Code\": \"\",\n" +
                        "                \"State/Province\": \"\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"aliases\": [\n" +
                        "            {\n" +
                        "                \"Category\": \"weak\",\n" +
                        "                \"Name\": \"Migue\",\n" +
                        "                \"Type\": \"a.k.a.\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"docs\": [\n" +
                        "            {\n" +
                        "                \"Country\": \"Pais de prueba\",\n" +
                        "                \"Expire Date\": \"\",\n" +
                        "                \"ID#\": \"AS34512\",\n" +
                        "                \"Issue Date\": \"\",\n" +
                        "                \"Type\": \"Passport\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"ofac_nombre\": {\n" +
                        "        \"\": \"Ciudad de prueba, Pais de prueba\",\n" +
                        "        \"Citizenship:\": \"\",\n" +
                        "        \"Date of Birth:\": \"22 Jen 1975\",\n" +
                        "        \"First Name:\": \"Miguel Fernando\",\n" +
                        "        \"Last Name:\": \"Perez Gomez\",\n" +
                        "        \"List:\": \"Individual\",\n" +
                        "        \"Nationality:\": \"Nacionalidad de prueba\",\n" +
                        "        \"Place of Birth:\": \"Ciudad de prueba, Pais de prueba\",\n" +
                        "        \"Program:\": \"REALES BRITTO\",\n" +
                        "        \"Remarks:\": \"\",\n" +
                        "        \"Title:\": \"\",\n" +
                        "        \"Type:\": \"Individual\",\n" +
                        "        \"addresses\": [\n" +
                        "            {\n" +
                        "                \"Address\": \"\",\n" +
                        "                \"City\": \"\",\n" +
                        "                \"Country\": \"Pais de prueba\",\n" +
                        "                \"Postal Code\": \"\",\n" +
                        "                \"State/Province\": \"\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"aliases\": [\n" +
                        "            {\n" +
                        "                \"Category\": \"weak\",\n" +
                        "                \"Name\": \"Migue\",\n" +
                        "                \"Type\": \"a.k.a.\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"docs\": [\n" +
                        "            {\n" +
                        "                \"Country\": \"Pais de prueba\",\n" +
                        "                \"Expire Date\": \"\",\n" +
                        "                \"ID#\": \"AS34512\",\n" +
                        "                \"Issue Date\": \"\",\n" +
                        "                \"Type\": \"Passport\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"peps\": [\n" +
                        "        {\n" +
                        "            \"AKA\": \"\",\n" +
                        "            \"CATEGORIA\": \"Listas restrictivas\",\n" +
                        "            \"CODIGO\": \"OFAC_545468515\",\n" +
                        "            \"DIRECCION\": \"\",\n" +
                        "            \"ESTADO\": \"\",\n" +
                        "            \"FECHA_FINAL_ROL\": \"\",\n" +
                        "            \"FECHA_UPDATE\": \"Nov  7 2022 12:00AM\",\n" +
                        "            \"ID\": \"1082884409\",\n" +
                        "            \"NACIONALIDAD_PAISDEORIGEN\": \"\",\n" +
                        "            \"NOMBRECOMPLETO\": \"MIGUE\",\n" +
                        "            \"NOMBRE_DESCRIPTIVO_LISTA\": \"OFAC Lista Clinton\",\n" +
                        "            \"NOMBRE_LISTA\": \"Office of Foreign Assets Control OFAC\",\n" +
                        "            \"ORIGEN_LISTA\": \"INTERNACIONAL\",\n" +
                        "            \"PRIMER_APELLIDO\": \"\",\n" +
                        "            \"PRIMER_NOMBRE\": \"\",\n" +
                        "            \"RELACIONADO_CON\": \"\",\n" +
                        "            \"ROL_O_DESCRIPCION1\": \"\",\n" +
                        "            \"ROL_O_DESCRIPCION2\": \"\",\n" +
                        "            \"SEGUNDO_APELLIDO\": \"\",\n" +
                        "            \"SEGUNDO_NOMBRE\": \"\",\n" +
                        "            \"TIPO_ID\": \"C\",\n" +
                        "            \"TIPO_LISTA\": \"OFAC\",\n" +
                        "            \"TIPO_PERSONA\": \"\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"peps2\": true,\n" +
                        "    \"peps_denom\": [\n" +
                        "        {\n" +
                        "            \"AKA\": \"\",\n" +
                        "            \"CATEGORIA\": \"Listas restrictivas\",\n" +
                        "            \"CODIGO\": \"OFAC_545468515\",\n" +
                        "            \"DIRECCION\": \"\",\n" +
                        "            \"ESTADO\": \"\",\n" +
                        "            \"FECHA_FINAL_ROL\": \"\",\n" +
                        "            \"FECHA_UPDATE\": \"Nov  7 2022 12:00AM\",\n" +
                        "            \"ID\": \"1082884409\",\n" +
                        "            \"NACIONALIDAD_PAISDEORIGEN\": \"\",\n" +
                        "            \"NOMBRECOMPLETO\": \"MIGUE\",\n" +
                        "            \"NOMBRE_DESCRIPTIVO_LISTA\": \"OFAC Lista Clinton\",\n" +
                        "            \"NOMBRE_LISTA\": \"Office of Foreign Assets Control OFAC\",\n" +
                        "            \"ORIGEN_LISTA\": \"INTERNACIONAL\",\n" +
                        "            \"PRIMER_APELLIDO\": \"\",\n" +
                        "            \"PRIMER_NOMBRE\": \"\",\n" +
                        "            \"RELACIONADO_CON\": \"\",\n" +
                        "            \"ROL_O_DESCRIPCION1\": \"\",\n" +
                        "            \"ROL_O_DESCRIPCION2\": \"\",\n" +
                        "            \"SEGUNDO_APELLIDO\": \"\",\n" +
                        "            \"SEGUNDO_NOMBRE\": \"\",\n" +
                        "            \"TIPO_ID\": \"C\",\n" +
                        "            \"TIPO_LISTA\": \"OFAC\",\n" +
                        "            \"TIPO_PERSONA\": \"\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"personeriabog\": [\n" +
                        "        {\n" +
                        "            \"nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"sancion\": \"Sancion de prueba\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"policia\": true,\n" +
                        "    \"procuraduria\": [\n" +
                        "        {\n" +
                        "            \"datos\": [\n" +
                        "                {\n" +
                        "                    \"Instancias\": [\n" +
                        "                        {\n" +
                        "                            \"Autoridad\": \"PERSONERIA DE PRUEBA - DELEGADA ASUNTOS DISCIPLINARIOS IV\",\n" +
                        "                            \"Fecha providencia\": \"30-08-2016\",\n" +
                        "                            \"Nombre\": \"PRIMERA\",\n" +
                        "                            \"fecha efecto Juridicos\": \"04-04-2017\"\n" +
                        "                        }\n" +
                        "                    ],\n" +
                        "                    \"SIRI\": \"SIRI: 1213583\",\n" +
                        "                    \"Sanciones\": [\n" +
                        "                        {\n" +
                        "                            \"Clase sanción\": \"PRINCIPAL\",\n" +
                        "                            \"Entidad\": \"INSTITUTO DISTRITAL DE PATRIMONIO CULTURAL- PRUEBA (PRUEBA DC)    PRUEBA DC(PRUEBA DC)\",\n" +
                        "                            \"Sanción\": \"DESTITUCION\",\n" +
                        "                            \"Término\": \"\"\n" +
                        "                        }\n" +
                        "                    ]\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"delito\": \"ANTECEDENTES DISCIPLINARIOS\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"profesion\": {\n" +
                        "        \"abogado\": {},\n" +
                        "        \"abogados_judicial\": [],\n" +
                        "        \"anec\": {},\n" +
                        "        \"colpsic\": {},\n" +
                        "        \"comvezcol\": {},\n" +
                        "        \"conalpe\": {},\n" +
                        "        \"conaltel\": [],\n" +
                        "        \"consejopro\": {},\n" +
                        "        \"copnia\": {\n" +
                        "            \"certificate_number\": \"123585-346875\",\n" +
                        "            \"certificate_status\": \"VIGENTE\",\n" +
                        "            \"certificate_type\": \"MATRICULA PROFESIONAL\",\n" +
                        "            \"document_number\": \"111\",\n" +
                        "            \"document_type\": \"CEDULA DE CIUDADANIA\",\n" +
                        "            \"full_name\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"profession\": \"INGENIERIA AMBIENTAL\",\n" +
                        "            \"resolution_date\": \"7/13/2018\",\n" +
                        "            \"resolution_number\": \"328\"\n" +
                        "        },\n" +
                        "        \"cpae\": {},\n" +
                        "        \"cpbiol\": {},\n" +
                        "        \"cpiq\": {},\n" +
                        "        \"cpnt\": {},\n" +
                        "        \"cpqcol\": {},\n" +
                        "        \"jcc\": {},\n" +
                        "        \"rethus\": {}\n" +
                        "    },\n" +
                        "    \"proveedores_ficticios\": true,\n" +
                        "    \"rama\": {\n" +
                        "        \"armeniajepms\": true,\n" +
                        "        \"barranquillajepms\": true,\n" +
                        "        \"bogotajepms\": true,\n" +
                        "        \"bucaramangajepms\": true,\n" +
                        "        \"bugajepms\": true,\n" +
                        "        \"calijepms\": true,\n" +
                        "        \"cartagenajepms\": true,\n" +
                        "        \"florenciajepms\": true,\n" +
                        "        \"ibaguejepms\": true,\n" +
                        "        \"manizalesjepms\": true,\n" +
                        "        \"medellinjepms\": true,\n" +
                        "        \"monteriajepms\": true,\n" +
                        "        \"neivajepms\": true,\n" +
                        "        \"palmirajepms\": true,\n" +
                        "        \"pastojepms\": true,\n" +
                        "        \"pereirajepms\": true,\n" +
                        "        \"popayanjepms\": true,\n" +
                        "        \"quibdojepms\": true,\n" +
                        "        \"tunjajepms\": true,\n" +
                        "        \"villavicenciojepms\": true\n" +
                        "    },\n" +
                        "    \"rama_unificada\": [\n" +
                        "        {\n" +
                        "            \"actuaciones\": [\n" +
                        "                {\n" +
                        "                    \"actuacion\": \"Sentencia de Primera Instancia de Tutela\",\n" +
                        "                    \"anotacion\": \"SE CONCEDIO LA TUTELA\",\n" +
                        "                    \"cant\": 1,\n" +
                        "                    \"codRegla\": \"00                              \",\n" +
                        "                    \"conDocumentos\": false,\n" +
                        "                    \"consActuacion\": 1,\n" +
                        "                    \"fechaActuacion\": \"2011-03-09T00:00:00\",\n" +
                        "                    \"fechaFinal\": \"\",\n" +
                        "                    \"fechaInicial\": \"\",\n" +
                        "                    \"fechaRegistro\": \"2011-03-09T00:00:00\",\n" +
                        "                    \"idRegActuacion\": 1686213572,\n" +
                        "                    \"llaveProceso\": \"54001546887464684865\"\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"cantFilas\": -1,\n" +
                        "            \"claseProceso\": \"Tutelas\",\n" +
                        "            \"contenidoRadicacion\": \"\",\n" +
                        "            \"demandado\": [\n" +
                        "                \"Demandado de prueba\"\n" +
                        "            ],\n" +
                        "            \"demandante\": [\n" +
                        "                \"MIGUEL FERNANDO PEREZ GOMEZ\"\n" +
                        "            ],\n" +
                        "            \"departamento\": \"NORTE DE PRUEBA\",\n" +
                        "            \"despacho\": \"JUZGADO 004 PENAL MUNICIPAL DE PRUEBA \",\n" +
                        "            \"detalle\": true,\n" +
                        "            \"esPrivado\": false,\n" +
                        "            \"estado_actuacion\": \"SI\",\n" +
                        "            \"fechaConsulta\": \"2023-09-07T16:47:43\",\n" +
                        "            \"fechaProceso\": \"2011-03-03T00:00:00\",\n" +
                        "            \"fechaUltimaActuacion\": \"2011-03-09T00:00:00\",\n" +
                        "            \"idConexion\": 398,\n" +
                        "            \"idProceso\": 1584513,\n" +
                        "            \"idRegProceso\": 0,\n" +
                        "            \"is_demandado\": false,\n" +
                        "            \"llaveProceso\": \"54001546887464684865\",\n" +
                        "            \"ponente\": \"\",\n" +
                        "            \"recurso\": \"Sin Tipo de Recurso\",\n" +
                        "            \"subclaseProceso\": \"Sin Subclase de Proceso\",\n" +
                        "            \"sujetos\": [\n" +
                        "                {\n" +
                        "                    \"cant\": 0,\n" +
                        "                    \"esEmplazado\": false,\n" +
                        "                    \"idRegSujeto\": 0,\n" +
                        "                    \"identificacion\": \"\",\n" +
                        "                    \"nombreRazonSocial\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "                    \"tipoSujeto\": \"Demandante\"\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"sujetosProcesales\": \"Demandante: MIGUEL FERNANDO PEREZ GOMEZ | Demandado: Demandado de prueba | Ministerio Publico: MINISTERIO PUBLICO \",\n" +
                        "            \"tieneActuaciones\": true,\n" +
                        "            \"tipoProceso\": \"Especiales\",\n" +
                        "            \"ubicacion\": \"Despacho\",\n" +
                        "            \"ultimaActualizacion\": \"\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"registraduria_certificado\": {\n" +
                        "        \"cedula\": \"111\",\n" +
                        "        \"estado\": \"VIGENTE\",\n" +
                        "        \"fecha_exp\": \"20 DE DICIEMBRE DE 1999\",\n" +
                        "        \"lugar_exp\": \"MEDELLIN - ANTIOQUIA\",\n" +
                        "        \"nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\"\n" +
                        "    },\n" +
                        "    \"relacionados\": [\n" +
                        "        {\n" +
                        "            \"empresa\": {\n" +
                        "                \"razon_social\": \"EMPRESA DE PRUEBA S.A.S.\",\n" +
                        "                \"representacion_legal_y_vinculos\": [\n" +
                        "                    {\n" +
                        "                        \"no identificación\": \"111\",\n" +
                        "                        \"nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "                        \"tipo de vinculo\": \"Representante Legal - Principal\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            },\n" +
                        "            \"nit\": 900539452\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"reputacional\": {\n" +
                        "        \"news\": [\n" +
                        "            {\n" +
                        "                \"description\": \"Se encontro que aparecio en una noticia de prueba\",\n" +
                        "                \"idioma\": \"\",\n" +
                        "                \"keyword\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "                \"link\": \"Link\",\n" +
                        "                \"rank\": 2,\n" +
                        "                \"sentimiento\": \"\",\n" +
                        "                \"source\": \"google\",\n" +
                        "                \"title\": \"Titulo de prueba\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"notable\": {},\n" +
                        "        \"social\": [\n" +
                        "            {\n" +
                        "                \"description\": \"Ve el perfil de prueba de MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "                \"idioma\": \"\",\n" +
                        "                \"keyword\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "                \"link\": \"Link\",\n" +
                        "                \"rank\": 1,\n" +
                        "                \"sentimiento\": \"\",\n" +
                        "                \"source\": \"google\",\n" +
                        "                \"title\": \"Titulo de prueba\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"rndc\": [\n" +
                        "        {\n" +
                        "            \"Cedula Conductor\": \"111\",\n" +
                        "            \"Consecutivo\": \"300822183\",\n" +
                        "            \"Destino\": \"CIUDAD DE PRUEBA\",\n" +
                        "            \"Fecha Expedición\": \"2022/11/05\",\n" +
                        "            \"Fecha Hora Radicación\": \"2022/11/05 16:39:02\",\n" +
                        "            \"Nombre Empresa Transportadora\": \"EMPRESA TRANSPORTADORA DE PRUEBA\",\n" +
                        "            \"Nro de Radicado\": \"135415415\",\n" +
                        "            \"Origen\": \"CIUDAD DE PRUEBA DE ORIGEN\",\n" +
                        "            \"Placa\": \"WPT523\",\n" +
                        "            \"Placa Remolque\": \"S74236\",\n" +
                        "            \"Tipo Doc\": \"Manifiesto\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"rnmc\": {\n" +
                        "        \"apelacion\": \"NO\",\n" +
                        "        \"articulo\": \"Art. 35 Comportamientos que afectan las relaciones entre las personas y las autoridades\",\n" +
                        "        \"departamento\": \"DEPARTAMENTO DE PRUEBA\",\n" +
                        "        \"expediente\": \"25-875-6-1538-123\",\n" +
                        "        \"expendiente\": \"25-875-6-1538-123\",\n" +
                        "        \"fecha\": \"30/04/2020 05:45:00 p. m.\",\n" +
                        "        \"formato\": \"25875114022\",\n" +
                        "        \"identificación\": \"123\",\n" +
                        "        \"infractor\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "        \"municipio\": \"MUNICIPIO DE PRUEBA\"\n" +
                        "    },\n" +
                        "    \"rues\": true,\n" +
                        "    \"ruaf\": {\n" +
                        "        \"ARL\": [\n" +
                        "            {\n" +
                        "                \"Actividad Economica\": \"EMPRESAS DE PRUEBA DE TRANSPORTE URBANO\",\n" +
                        "                \"Administradora\": \"Seguros de Vida Ciudad de Prueba\",\n" +
                        "                \"Estado de Afiliación\": \"Activa\",\n" +
                        "                \"Fecha de Afiliación\": \"2022-05-01\",\n" +
                        "                \"Fecha de corte\": \"2022-11-04\",\n" +
                        "                \"Municipio Labora\": \"CIUDAD DE PRUEBA\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"Basico\": [\n" +
                        "            {\n" +
                        "                \"Fecha de corte\": \"2022-11-04\",\n" +
                        "                \"Número de Identificación\": \"CC 123\",\n" +
                        "                \"Primer Apellido\": \"PEREZ\",\n" +
                        "                \"Primer Nombre\": \"MIGUEL\",\n" +
                        "                \"Segundo Apellido\": \"GOMEZ\",\n" +
                        "                \"Segundo Nombre\": \"FERNANDO\",\n" +
                        "                \"Sexo\": \"M\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"Caja de compensación\": [\n" +
                        "            {\n" +
                        "                \"Administradora CF\": \"CAJA DE PRUEBA DE CIUDAD DE PRUEBA\",\n" +
                        "                \"Estado de Afiliación\": \"Inactivo\",\n" +
                        "                \"Fecha de Afiliación\": \"2017-07-21\",\n" +
                        "                \"Fecha de corte\": \"2022-11-04\",\n" +
                        "                \"Municipio Labora\": \"CIUDAD DE PRUEBA\",\n" +
                        "                \"Tipo de Afiliado\": \"Trabajador afiliado dependiente\",\n" +
                        "                \"Tipo de Miembro de la Población Cubierta\": \"Afiliado\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"Administradora CF\": \"CAJA DE PRUEBA DE CIUDAD DE PRUEBA\",\n" +
                        "                \"Estado de Afiliación\": \"Inactivo\",\n" +
                        "                \"Fecha de Afiliación\": \"2020-03-17\",\n" +
                        "                \"Fecha de corte\": \"2022-11-04\",\n" +
                        "                \"Municipio Labora\": \"CIUDAD DE PRUEBA\",\n" +
                        "                \"Tipo de Afiliado\": \"Trabajador afiliado dependiente\",\n" +
                        "                \"Tipo de Miembro de la Población Cubierta\": \"Afiliado\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"Cesantías\": [\n" +
                        "            {\n" +
                        "                \"Cesantías\": \"No se encontraron afiliaciones\",\n" +
                        "                \"Fecha de corte\": \"2022-11-04\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"Pensionado\": [\n" +
                        "            {\n" +
                        "                \"Fecha de corte\": \"2022-11-04\",\n" +
                        "                \"Pensionado\": \"No se encontraron afiliaciones\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"Pensiones\": [\n" +
                        "            {\n" +
                        "                \"Administradora\": \"COMPAÑIA DE PRUEBA DE CIUDAD DE PRUEBA\",\n" +
                        "                \"Estado de Afiliación\": \"Activo cotizante\",\n" +
                        "                \"Fecha de Afiliación\": \"2007-03-01\",\n" +
                        "                \"Fecha de corte\": \"2022-11-04\",\n" +
                        "                \"Régimen\": \"PENSIONES: AHORRO INDIVIDUAL\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"Programas de Asistencia Social\": [\n" +
                        "            {\n" +
                        "                \"Fecha de corte\": \"2022-11-04\",\n" +
                        "                \"Programas de Asistencia Social\": \"No se encontraron afiliaciones\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"Salud\": [\n" +
                        "            {\n" +
                        "                \"Administradora\": \"ENTIDAD DE PRUEBA DE CIUDAD DE PRUEBA\",\n" +
                        "                \"Departamento -> Municipio\": \"CIUDAD DE PRUEBA\",\n" +
                        "                \"Estado de Afiliación\": \"Activo\",\n" +
                        "                \"Fecha Afiliacion\": \"31/05/2022\",\n" +
                        "                \"Fecha de corte\": \"2022-11-04\",\n" +
                        "                \"Régimen\": \"Contributivo\",\n" +
                        "                \"Tipo de Afiliado\": \"COTIZANTE\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"runt_app\": {\n" +
                        "        \"exitoso\": true,\n" +
                        "        \"licencia\": {\n" +
                        "            \"licencias\": [\n" +
                        "                {\n" +
                        "                    \"categoria\": \"A2\",\n" +
                        "                    \"estado\": \"ACTIVA\",\n" +
                        "                    \"fecha_expedicion\": \"16/07/2021\",\n" +
                        "                    \"fecha_vencimiento\": \"16/07/2031\",\n" +
                        "                    \"numero_licencia\": \"1082854555\",\n" +
                        "                    \"sustrato\": \"LC02045482\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                    \"categoria\": \"B1\",\n" +
                        "                    \"estado\": \"ACTIVA\",\n" +
                        "                    \"fecha_expedicion\": \"14/03/2018\",\n" +
                        "                    \"fecha_vencimiento\": \"14/03/2028\",\n" +
                        "                    \"numero_licencia\": \"1082854555\",\n" +
                        "                    \"sustrato\": \"LC02045482\"\n" +
                        "                }\n" +
                        "            ],\n" +
                        "            \"totalLicencias\": 2\n" +
                        "        },\n" +
                        "        \"multa\": {\n" +
                        "            \"estado_cancelacion\": \"NO\",\n" +
                        "            \"estado_paz_salvo\": \"SI\",\n" +
                        "            \"estado_suspension\": \"\",\n" +
                        "            \"fecha_cancelacion\": \"No Reporta\",\n" +
                        "            \"fecha_suspension\": \"No Reporta\",\n" +
                        "            \"numero_comparendos\": \"0\",\n" +
                        "            \"numero_paz_salvo\": \"562454684875\"\n" +
                        "        },\n" +
                        "        \"nombres\": \"MIGUEL FERNANDO PEREZ GOMEZ\"\n" +
                        "    },\n" +
                        "    \"rut\": \"111-1\",\n" +
                        "    \"rut_estado\": \"REGISTRO ACTIVO\",\n" +
                        "    \"secop2\": [\n" +
                        "        {\n" +
                        "            \"anno_bpin\": \"2021\",\n" +
                        "            \"c_digo_bpin\": \"2021004689999\",\n" +
                        "            \"ciudad\": \"Ciudad Prueba\",\n" +
                        "            \"codigo_de_categoria_principal\": \"V1.99999999\",\n" +
                        "            \"codigo_entidad\": \"701515999\",\n" +
                        "            \"codigo_proveedor\": \"713213999\",\n" +
                        "            \"condiciones_de_entrega\": \"Según acuerdo ficticio\",\n" +
                        "            \"departamento\": \"Departamento Prueba\",\n" +
                        "            \"descripcion_del_proceso\": \"PRESTAR SERVICIOS DE APOYO EN EL DEPARTAMENTO DE PRUEBA\",\n" +
                        "            \"destino_gasto\": \"Inversión Prueba\",\n" +
                        "            \"dias_adicionados\": \"0\",\n" +
                        "            \"documento_proveedor\": \"1234567890\",\n" +
                        "            \"entidad_centralizada\": \"Centralizada\",\n" +
                        "            \"es_grupo\": \"No\",\n" +
                        "            \"es_pyme\": \"No\",\n" +
                        "            \"espostconflicto\": \"No\",\n" +
                        "            \"estado_bpin\": \"Válido\",\n" +
                        "            \"estado_contrato\": \"En prueba\",\n" +
                        "            \"fecha_de_fin_del_contrato\": \"2023-05-26T00:00:00.000\",\n" +
                        "            \"fecha_de_firma\": \"2022-12-27T17:12:41.000\",\n" +
                        "            \"fecha_de_inicio_del_contrato\": \"2022-12-27T00:00:00.000\",\n" +
                        "            \"fecha_fin_liquidacion\": \"2023-09-25 04:59:00\",\n" +
                        "            \"fecha_inicio_liquidacion\": \"2023-05-25 04:59:00\",\n" +
                        "            \"g_nero_representante_legal\": \"Hombre\",\n" +
                        "            \"habilita_pago_adelantado\": \"No\",\n" +
                        "            \"id_contrato\": \"CO1.PCCNTR.9999999\",\n" +
                        "            \"identificaci_n_representante_legal\": \"1234567890\",\n" +
                        "            \"justificacion_modalidad_de\": \"ServiciosFicticios\",\n" +
                        "            \"liquidaci_n\": \"Si\",\n" +
                        "            \"localizaci_n\": \"Ciudad Prueba,  Departamento Prueba ,  Ciudad Prueba\",\n" +
                        "            \"modalidad_de_contratacion\": \"Contratación ficticia\",\n" +
                        "            \"nacionalidad_representante_legal\": \"Prueba\",\n" +
                        "            \"nit_entidad\": \"890299999\",\n" +
                        "            \"nombre_entidad\": \"DEPARTAMENTO DE PRUEBA\",\n" +
                        "            \"nombre_representante_legal\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"objeto_del_contrato\": \"PRESTAR SERVICIOS DE APOYO EN EL DEPARTAMENTO DE PRUEBA\",\n" +
                        "            \"obligaci_n_ambiental\": \"No\",\n" +
                        "            \"obligaciones_postconsumo\": \"No\",\n" +
                        "            \"orden\": \"Territorial\",\n" +
                        "            \"origen_de_los_recursos\": \"Distribuido\",\n" +
                        "            \"pilares_del_acuerdo\": \"No aplica\",\n" +
                        "            \"presupuesto_general_de_la_nacion_pgn\": \"0\",\n" +
                        "            \"proceso_de_compra\": \"CO1.BDOS.9999999\",\n" +
                        "            \"proveedor_adjudicado\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"puntos_del_acuerdo\": \"No aplica\",\n" +
                        "            \"rama\": \"Ejecutivo\",\n" +
                        "            \"recursos_de_credito\": \"0\",\n" +
                        "            \"recursos_propios\": \"0\",\n" +
                        "            \"recursos_propios_alcald_as_gobernaciones_y_resguardos_ind_genas_\": \"99999999\",\n" +
                        "            \"referencia_del_contrato\": \"CO1.PCCNTR.9999999\",\n" +
                        "            \"reversion\": \"No\",\n" +
                        "            \"saldo_cdp\": \"9999999999\",\n" +
                        "            \"saldo_vigencia\": \"9999999999\",\n" +
                        "            \"sector\": \"Servicio Ficticio\",\n" +
                        "            \"sistema_general_de_participaciones\": \"0\",\n" +
                        "            \"sistema_general_de_regal_as\": \"0\",\n" +
                        "            \"tipo_de_contrato\": \"Servicio Ficticio\",\n" +
                        "            \"tipo_de_identificaci_n_representante_legal\": \"Cédula Ficticia\",\n" +
                        "            \"tipodocproveedor\": \"Cédula Ficticia\",\n" +
                        "            \"ultima_actualizacion\": \"2023-03-23T00:00:00.000\",\n" +
                        "            \"urlproceso\": {\n" +
                        "                \"url\": \"link\"\n" +
                        "            },\n" +
                        "            \"valor_amortizado\": \"0\",\n" +
                        "            \"valor_de_pago_adelantado\": \"0\",\n" +
                        "            \"valor_del_contrato\": \"99999999\",\n" +
                        "            \"valor_facturado\": \"99999999\",\n" +
                        "            \"valor_pagado\": \"99999999\",\n" +
                        "            \"valor_pendiente_de\": \"0\",\n" +
                        "            \"valor_pendiente_de_ejecucion\": \"0\",\n" +
                        "            \"valor_pendiente_de_pago\": \"0\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"secop\": [\n" +
                        "        {\n" +
                        "            \"cuantia_proceso\": \"156526328\",\n" +
                        "            \"detalle_del_objeto_a_contratar\": \"Detalle de prueba.\",\n" +
                        "            \"estado_del_proceso\": \"Celebrado\",\n" +
                        "            \"fecha_de_firma_del_contrato\": \"27/06/2019\",\n" +
                        "            \"nom_raz_social_contratista\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"nombre_del_represen_legal\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"ruta_proceso_en_secop_i\": {\n" +
                        "                \"url\": \"Link\"\n" +
                        "            },\n" +
                        "            \"tipo_de_contrato\": \"Prestación de Servicios\",\n" +
                        "            \"tipo_de_proceso\": \"Contratación Directa (Ley 1150 de 2007)\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"secop_s\": [\n" +
                        "        {\n" +
                        "            \"descripcion_contrato\": \"Descripccion de prueba\",\n" +
                        "            \"documento_contratista\": \"111\",\n" +
                        "            \"fecha_de_publicacion\": \"20-07-2023\",\n" +
                        "            \"municipio\": \"CIUDAD PRUEBA - PROVINCIA PRUEBA\",\n" +
                        "            \"nit_entidad\": \"999999999\",\n" +
                        "            \"nivel\": \"Nivel Ficticio\",\n" +
                        "            \"nombre_contratista\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                        "            \"nombre_entidad\": \"AGENCIA FICTICIA DE ESTRATEGIAS LEGALES (AFEL)\",\n" +
                        "            \"numero_de_contrato\": \"456-2023\",\n" +
                        "            \"numero_de_resolucion\": \"254-2023\",\n" +
                        "            \"numero_proceso\": \"5432\",\n" +
                        "            \"orden\": \"Orden Ficticio\",\n" +
                        "            \"ruta_de_proceso\": \"Link\",\n" +
                        "            \"tipo_documento\": \"Cc\",\n" +
                        "            \"tipo_incumplimiento\": \"Incumplimiento Ficticio\",\n" +
                        "            \"valor_sancion\": \"$9,999,999\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"sena\": [\n" +
                        "        {\n" +
                        "            \"Certificación\": \"2023/07/31 12:47:14.0\",\n" +
                        "            \"Descarga\": \"Link\",\n" +
                        "            \"Estado de Certificación\": \"Firmado\",\n" +
                        "            \"Estado del Aprendiz\": \"Certificado\",\n" +
                        "            \"Programa\": \"2781092 - COMPLEMENTARIA VIRTUAL EN CERTIFICACION DE PRUEBA  Y  EL REGISTRO PARA LOCALIZACION Y CARACTERIZACION DE LAS PERSONAS CON ASUNTOS DE PRUEBAS\",\n" +
                        "            \"Registro\": \"9123002781092CC164168461E\",\n" +
                        "            \"Tipo\": \"Nota Especial\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"sigep\": {\n" +
                        "        \"Experiencia Laboral\": [\n" +
                        "            {\n" +
                        "                \"Rangos de salario por nivel\": \"Escala\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"Rangos de salario por nivel\": \"ASESOR\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"Rangos de salario por nivel\": \"ASESOR\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"Formación Académica\": [\n" +
                        "            \"ONCE - NO APLICA - Graduado\",\n" +
                        "            \"PROFESIONAL - NO APLICA - Graduado\"\n" +
                        "        ],\n" +
                        "        \"Informacion basica\": {\n" +
                        "            \"cargo_funcionario\": \"No Reportado\",\n" +
                        "            \"dependencia_funcionario\": \"GRUPO DE VIGILANCIA DE PRUEBA\",\n" +
                        "            \"institucion_funcionario\": \"CONTRALORIA GENERAL DE PRUEBA\",\n" +
                        "            \"nombre_funcionario\": \"MIGUEL FERNANDO PEREZ GOMEZ\"\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"simit\": {\n" +
                        "        \"acuerdos_pagar\": 0,\n" +
                        "        \"acuerdos_pagos\": [],\n" +
                        "        \"cantidad_multas\": 0,\n" +
                        "        \"cursos\": [\n" +
                        "            {\n" +
                        "                \"centro_intruccion\": \"CENTRO DE PRUEBA\",\n" +
                        "                \"certificado\": \"26201\",\n" +
                        "                \"cuidad\": \"CIUDAD DE PRUEBA\",\n" +
                        "                \"estado\": \"No aplicado\",\n" +
                        "                \"fecha_comparendo\": \"\",\n" +
                        "                \"fecha_curso\": \"09/08/2011\",\n" +
                        "                \"fecha_reporte\": \"\",\n" +
                        "                \"numero_multa\": \"11001000000541564521\"\n" +
                        "            }\n" +
                        "        ],\n" +
                        "        \"multas\": [],\n" +
                        "        \"numero_documento\": \"111\",\n" +
                        "        \"paz_salvo\": true,\n" +
                        "        \"total_acuardos_por_pagar\": 0,\n" +
                        "        \"total_general\": 0,\n" +
                        "        \"total_multas\": 0,\n" +
                        "        \"total_multas_pagar\": 0,\n" +
                        "        \"total_pagar\": 0\n" +
                        "    },\n" +
                        "    \"simur\": [\n" +
                        "        {\n" +
                        "            \"Estado comparendo\": \"NO NOTIFICADO DEI\",\n" +
                        "            \"Fecha Infraccion\": \"09/07/2023\",\n" +
                        "            \"Intereses\": \"$000\",\n" +
                        "            \"No Comparendo\": \"11001000000039204546878\",\n" +
                        "            \"Placa\": \"WRM553\",\n" +
                        "            \"Saldo\": \"$522,90000\",\n" +
                        "            \"Tipo\": \"COMPARENDO -ELECTRONICO\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"sirna\": [\n" +
                        "        {\n" +
                        "            \"apellidos\": \"PEREZ GOMEZ\",\n" +
                        "            \"codigo_de_estado_de_sancion\": \"9521354811\",\n" +
                        "            \"estado_de_la_sancion\": \"Sanción en Ejecución\",\n" +
                        "            \"fecha_de_finalizacion\": \"02/02/2024\",\n" +
                        "            \"fecha_de_inicio\": \"03/08/2023\",\n" +
                        "            \"nombres\": \"MIGUEL FERNANDO\",\n" +
                        "            \"numero_de_cedula\": \"111\",\n" +
                        "            \"tipo_de_sancion\": \"Suspensión\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"sisben\": {\n" +
                        "        \"Actualizacion Ciudadano\": \"20/05/2019\",\n" +
                        "        \"Apellido\": \"PEREZ GOMEZ\",\n" +
                        "        \"Departamento\": \"DEPARTAMENTO DE PRUEBA\",\n" +
                        "        \"Estado\": \"Registro válido\",\n" +
                        "        \"Ficha\": \"1145312382135141\",\n" +
                        "        \"Grupo\": \"C3\",\n" +
                        "        \"Municipio\": \"MUNICIPIO DE PRUEBA\",\n" +
                        "        \"Nombre\": \"MIGUEL FERNANDO\",\n" +
                        "        \"Tipo de grupo\": \"Vulnerable\"\n" +
                        "    },\n" +
                        "    \"sisconmp\": {\n" +
                        "        \"Apellidos\": \"PEREZ GOMEZ\",\n" +
                        "        \"Clase\": \"\",\n" +
                        "        \"DCInstitucionEducativa\": 3,\n" +
                        "        \"DIVCODIGSede\": \"5000145412\",\n" +
                        "        \"DIVNOMBRSede\": \"CIUDAD DE PRUEBA\",\n" +
                        "        \"DescripcionClase\": \"\",\n" +
                        "        \"EntidadCertificadora\": \"MEN\",\n" +
                        "        \"FechaExpedicion\": \"2022/11/01\",\n" +
                        "        \"FechaExpedicionLicencia\": \"2017/09/15\",\n" +
                        "        \"FechaVencimiento\": \"2023/11/01\",\n" +
                        "        \"FechaVencimientoLicencia\": \"2029/09/13\",\n" +
                        "        \"Inactivo\": \"No\",\n" +
                        "        \"InstitucionEducativa\": \"CEA AUTO PRUEBA\",\n" +
                        "        \"NDI\": \"79765445485\",\n" +
                        "        \"NIDSede\": \"75182\",\n" +
                        "        \"NITInstitucionEducativa\": 401235454,\n" +
                        "        \"NombreArchivo\": \"\",\n" +
                        "        \"NombreCapacitacion\": \"ACTUALIZACION\",\n" +
                        "        \"NombreSede\": \"CEA AUTO PRUEBA\",\n" +
                        "        \"Nombres\": \"MIGUEL FERNANDO\",\n" +
                        "        \"NumeroLicencia\": \"4684874545\",\n" +
                        "        \"TDI\": \"CC\",\n" +
                        "        \"TipoCapacitacion\": \"CURSO BASICO\",\n" +
                        "        \"TipoVehiculo\": \"\",\n" +
                        "        \"ValorNumericoClase\": 0\n" +
                        "    },\n" +
                        "    \"transitobog\": true\n");
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
                .setBody(this.launchBody());
    }

    private String launchBody(){
        return "{\n" +
                "    \"email\": \"usuario@pruebas.com\",\n" +
                "    \"doc\": 111,\n" +
                "    \"jobid\": \"6460fc34-4154-43db-9438-8c5a059304c0\",\n" +
                "    \"nombre\": \"MIGUEL FERNANDO PEREZ GOMEZ\",\n" +
                "    \"typedoc\": \"CC\",\n" +
                "    \"validado\": true\n" +
                "}";
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