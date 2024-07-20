package io.github.sashirestela.openai.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.sashirestela.openai.common.function.SchemaConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static io.github.sashirestela.openai.support.JsonSchemaUtil.JSON_EMPTY_CLASS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomSchemaConverterTest {

    private static SchemaConverter schemaConverter = new CustomSchemaConverter();

    @Test
    void shouldGenerateFullJsonSchemaWhenClassHasSomeFields() {
        var actualJsonSchema = schemaConverter.convert(TestClass.class).toString();
        var expectedJsonSchema = "{\"type\":\"object\",\"properties\":{\"first\":{\"type\":\"string\",\"myCustomProperty\":true},\"second\":{\"type\":\"integer\",\"myCustomProperty\":true}},\"required\":[\"first\"],\"myCustomProperty\":true}";
        assertEquals(expectedJsonSchema, actualJsonSchema);
    }

    @Test
    void shouldGenerateEmptyJsonSchemaWhenClassHasNoFields() {
        var actualJsonSchema = schemaConverter.convert(EmptyClass.class).toString();
        var expectedJsonSchema = JSON_EMPTY_CLASS;
        assertEquals(expectedJsonSchema, actualJsonSchema);
    }

    @Test
    void shouldGenerateOrderedJsonSchemaWhenClassHasJsonPropertyOrderAnnotation() {
        var actualJsonSchema = schemaConverter.convert(OrderedTestClass.class).toString();
        var expectedJsonSchema = "{\"type\":\"object\",\"properties\":{\"first\":{\"type\":\"string\",\"myCustomProperty\":true},\"second\":{\"type\":\"integer\",\"myCustomProperty\":true},\"third\":{\"type\":\"string\",\"myCustomProperty\":true}},\"required\":[\"first\"],\"myCustomProperty\":true}";
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
