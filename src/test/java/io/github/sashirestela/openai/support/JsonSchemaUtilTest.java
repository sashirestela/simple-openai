package io.github.sashirestela.openai.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static io.github.sashirestela.openai.support.JsonSchemaUtil.JSON_EMPTY_CLASS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonSchemaUtilTest {

    @Test
    void shouldGenerateFullJsonSchemaWhenClassHasSomeFields() {
        var actualJsonSchema = JsonSchemaUtil.classToJsonSchema(TestClass.class).toString();
        var expectedJsonSchema = "{\"type\":\"object\",\"properties\":{\"first\":{\"type\":\"string\"}," +
                "\"second\":{\"type\":\"integer\"}},\"required\":[\"first\"],\"additionalProperties\":false}";
        assertEquals(expectedJsonSchema, actualJsonSchema);
    }

    @Test
    void shouldGenerateEmptyJsonSchemaWhenClassHasNoFields() {
        var actualJsonSchema = JsonSchemaUtil.classToJsonSchema(EmptyClass.class).toString();
        var expectedJsonSchema = JSON_EMPTY_CLASS;
        assertEquals(expectedJsonSchema, actualJsonSchema);
    }

    @Test
    void shouldGenerateOrderedJsonSchemaWhenClassHasJsonPropertyOrderAnnotation() {
        var actualJsonSchema = JsonSchemaUtil.classToJsonSchema(OrderedTestClass.class).toString();
        var expectedJsonSchema = "{\"type\":\"object\",\"properties\":{\"first\":{\"type\":\"string\"}," +
                "\"second\":{\"type\":\"integer\"}," +
                "\"third\":{\"type\":\"string\"}}," +
                "\"required\":[\"first\"],\"additionalProperties\":false}";
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

    static class EmptyClass {
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @JsonPropertyOrder({ "first", "second", "third" })
    static class OrderedTestClass {

        @JsonProperty(required = true)
        public String first;

        public Integer second;

        public String third;

    }

}
