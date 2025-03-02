package com.tusdatos.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tusdatos.dto.request.LaunchRequestDTO;
import org.junit.jupiter.api.Test;

class JacksonUtilsTest {

    @Test
    void given_a_json_transform_to_object() throws JsonProcessingException {
        System.out.println(JacksonUtils.objectToJson(new LaunchRequestDTO()));
        System.out.println(JacksonUtils.jsonToObject("{\n" +
                "  \"doc\": 123,\n" +
                "  \"typedoc\": \"CC\",\n" +
                "  \"fechaE\": \"01/12/2017\"\n" +
                "}", LaunchRequestDTO.class));
    }

}