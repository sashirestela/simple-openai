package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.sashirestela.openai.common.ResponseFormat.ResponseFormatType;
import io.github.sashirestela.openai.domain.response.ResponseText.ResponseTextFormat.ResponseTextFormatJsonSchema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResponseTextTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testTextFactory() {
        // Create ResponseText using the text() factory method
        var responseText = ResponseText.text();

        // Verify the format type is TEXT
        assertNotNull(responseText.getFormat());
        assertEquals(ResponseFormatType.TEXT, responseText.getFormat().getType());

        // Verify attributes
        assertNotNull(responseText.toString());
    }

    @Test
    void testJsonObjectFactory() {
        // Create ResponseText using the jsonObject() factory method
        var responseText = ResponseText.jsonObject();

        // Verify the format type is JSON_OBJECT
        assertNotNull(responseText.getFormat());
        assertEquals(ResponseFormatType.JSON_OBJECT, responseText.getFormat().getType());

        // Verify attributes
        assertNotNull(responseText.toString());
    }

    @Test
    void testJsonSchemaFactoryWithNodeSchema() {
        // Create a JsonNode schema
        ObjectNode schema = mapper.createObjectNode();
        ObjectNode properties = mapper.createObjectNode();

        // Add properties to the schema
        ObjectNode nameProp = mapper.createObjectNode();
        nameProp.put("type", "string");
        nameProp.put("description", "Person's name");

        ObjectNode ageProp = mapper.createObjectNode();
        ageProp.put("type", "integer");
        ageProp.put("minimum", 0);

        properties.set("name", nameProp);
        properties.set("age", ageProp);

        schema.put("type", "object");
        schema.set("properties", properties);
        schema.set("required", mapper.createArrayNode().add("name"));

        // Create the JsonSchema format
        var jsonSchemaFormat = ResponseTextFormatJsonSchema.builder()
                .name("Person")
                .schema(schema)
                .description("A person object")
                .strict(true)
                .build();

        // Create ResponseText using the jsonSchema() factory method
        var responseText = ResponseText.jsonSchema(jsonSchemaFormat);

        // Verify the format type and properties
        assertNotNull(responseText.getFormat());
        assertEquals(ResponseFormatType.JSON_SCHEMA, responseText.getFormat().getType());
        assertEquals("Person", ((ResponseTextFormatJsonSchema) responseText.getFormat()).getName());
        assertEquals("A person object", ((ResponseTextFormatJsonSchema) responseText.getFormat()).getDescription());
        assertEquals(true, ((ResponseTextFormatJsonSchema) responseText.getFormat()).getStrict());
        assertNotNull(((ResponseTextFormatJsonSchema) responseText.getFormat()).getSchema());
    }

    @Test
    void testJsonSchemaFactoryWithClassSchema() {
        // Create ResponseTextFormatJsonSchema with a class reference
        var jsonSchemaFormat = ResponseTextFormatJsonSchema.builder()
                .name("TestPerson")
                .schemaClass(TestPerson.class)
                .description("A test person class")
                .strict(false)
                .build();

        // Create ResponseText using the jsonSchema() factory method
        var responseText = ResponseText.jsonSchema(jsonSchemaFormat);

        // Verify the format type and properties
        assertNotNull(responseText.getFormat());
        assertEquals(ResponseFormatType.JSON_SCHEMA, responseText.getFormat().getType());
        assertEquals("TestPerson", ((ResponseTextFormatJsonSchema) responseText.getFormat()).getName());
        assertEquals("A test person class", ((ResponseTextFormatJsonSchema) responseText.getFormat()).getDescription());
        assertEquals(false, ((ResponseTextFormatJsonSchema) responseText.getFormat()).getStrict());
        assertNotNull(((ResponseTextFormatJsonSchema) responseText.getFormat()).getSchema());
    }

    @Test
    void testJsonSchemaWithoutSchema() {
        // Create ResponseTextFormatJsonSchema with a null schema and null class
        var jsonSchemaFormat = ResponseTextFormatJsonSchema.builder()
                .name("EmptySchema")
                .build();

        // Create ResponseText using the jsonSchema() factory method
        var responseText = ResponseText.jsonSchema(jsonSchemaFormat);

        // Verify the format type and properties
        assertNotNull(responseText.getFormat());
        assertEquals(ResponseFormatType.JSON_SCHEMA, responseText.getFormat().getType());
        assertEquals("EmptySchema", ((ResponseTextFormatJsonSchema) responseText.getFormat()).getName());
        assertNull(((ResponseTextFormatJsonSchema) responseText.getFormat()).getDescription());
        assertNull(((ResponseTextFormatJsonSchema) responseText.getFormat()).getStrict());
        assertNull(((ResponseTextFormatJsonSchema) responseText.getFormat()).getSchema());
    }

    // Test class for schema generation
    private static class TestPerson {

        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }

}
