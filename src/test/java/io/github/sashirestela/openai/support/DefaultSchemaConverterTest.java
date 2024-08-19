package io.github.sashirestela.openai.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultSchemaConverterTest {

    @Test
    void testConvertStandard() {
        var actualJsonSchema = new DefaultSchemaConverter().convert(TestClass.class).toString();
        var expectedJsonSchema = "{\"type\":\"object\",\"properties\":{\"first\":{\"type\":\"string\"}," +
                "\"second\":{\"type\":\"integer\"}},\"required\":[\"first\"],\"additionalProperties\":false}";
        assertEquals(expectedJsonSchema, actualJsonSchema);

    }

    @Test
    void testConvertStructuredOutput() {
        var actualJsonSchema = new DefaultSchemaConverter(Boolean.TRUE).convert(TestClass.class).toString();
        var expectedJsonSchema = "{\"type\":\"object\",\"properties\":{\"first\":{\"type\":\"string\"}," +
                "\"second\":{\"type\":\"integer\"}},\"required\":[\"first\",\"second\"],\"additionalProperties\":false}";
        assertEquals(expectedJsonSchema, actualJsonSchema);

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    static class TestClass {

        @JsonProperty(required = true)
        public String first;

        public Integer second;

    }

}
