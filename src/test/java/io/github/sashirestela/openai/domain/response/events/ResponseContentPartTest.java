package io.github.sashirestela.openai.domain.response.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseContentPartTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testResponseContentPartDeserialization() {
        // Create a JSON for ResponseContentPart
        String json = "{\n" +
            "  \"type\": \"response.content_part.added\",\n" +
            "  \"itemId\": \"item_abc123\",\n" +
            "  \"outputIndex\": \"0\",\n" +
            "  \"contentIndex\": \"0\",\n" +
            "  \"part\": {\n" +
            "    \"type\": \"text\",\n" +
            "    \"text\": \"This is a text content part.\"\n" +
            "  }\n" +
            "}";

        try {
            // Deserialize
            ResponseContentPart contentPart = mapper.readValue(json, ResponseContentPart.class);

            // Verify
            assertNotNull(contentPart);
            assertEquals("response.content_part.added", contentPart.getType());
            assertEquals("item_abc123", contentPart.getItemId());
            assertEquals("0", contentPart.getOutputIndex());
            assertEquals("0", contentPart.getContentIndex());
            assertNotNull(contentPart.getPart());
            assertEquals("text", contentPart.getPart().get("type"));
            assertEquals("This is a text content part.", contentPart.getPart().get("text"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize ResponseContentPart", e);
        }
    }

    @Test
    void testResponseContentPartWithImagePart() {
        // Create a JSON for ResponseContentPart with an image part
        String json = "{\n" +
            "  \"type\": \"response.content_part.added\",\n" +
            "  \"itemId\": \"item_image123\",\n" +
            "  \"outputIndex\": \"0\",\n" +
            "  \"contentIndex\": \"1\",\n" +
            "  \"part\": {\n" +
            "    \"type\": \"image\",\n" +
            "    \"image\": {\n" +
            "      \"url\": \"https://example.com/image.jpg\",\n" +
            "      \"detail\": \"high\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

        try {
            // Deserialize
            ResponseContentPart contentPart = mapper.readValue(json, ResponseContentPart.class);

            // Verify
            assertNotNull(contentPart);
            assertEquals("response.content_part.added", contentPart.getType());
            assertEquals("item_image123", contentPart.getItemId());
            assertEquals("0", contentPart.getOutputIndex());
            assertEquals("1", contentPart.getContentIndex());
            assertNotNull(contentPart.getPart());
            assertEquals("image", contentPart.getPart().get("type"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> image = (Map<String, Object>) contentPart.getPart().get("image");
            assertNotNull(image);
            assertEquals("https://example.com/image.jpg", image.get("url"));
            assertEquals("high", image.get("detail"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize ResponseContentPart with image", e);
        }
    }

    @Test
    void testResponseContentPartWithToolCallPart() {
        // Create a JSON for ResponseContentPart with a tool call part
        String json = "{\n" +
            "  \"type\": \"response.content_part.added\",\n" +
            "  \"itemId\": \"item_tool123\",\n" +
            "  \"outputIndex\": \"0\",\n" +
            "  \"contentIndex\": \"2\",\n" +
            "  \"part\": {\n" +
            "    \"type\": \"tool_call\",\n" +
            "    \"tool_call\": {\n" +
            "      \"id\": \"call_abc123\",\n" +
            "      \"type\": \"function\",\n" +
            "      \"function\": {\n" +
            "        \"name\": \"get_weather\",\n" +
            "        \"arguments\": \"{\\\"location\\\":\\\"New York\\\"}\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

        try {
            // Deserialize
            ResponseContentPart contentPart = mapper.readValue(json, ResponseContentPart.class);

            // Verify
            assertNotNull(contentPart);
            assertEquals("response.content_part.added", contentPart.getType());
            assertEquals("item_tool123", contentPart.getItemId());
            assertEquals("0", contentPart.getOutputIndex());
            assertEquals("2", contentPart.getContentIndex());
            assertNotNull(contentPart.getPart());
            assertEquals("tool_call", contentPart.getPart().get("type"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> toolCall = (Map<String, Object>) contentPart.getPart().get("tool_call");
            assertNotNull(toolCall);
            assertEquals("call_abc123", toolCall.get("id"));
            assertEquals("function", toolCall.get("type"));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> function = (Map<String, Object>) toolCall.get("function");
            assertNotNull(function);
            assertEquals("get_weather", function.get("name"));
            assertEquals("{\"location\":\"New York\"}", function.get("arguments"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize ResponseContentPart with tool call", e);
        }
    }

    @Test
    void testResponseContentPartCreation() {
        // Create a ResponseContentPart by manually setting fields
        ResponseContentPart contentPart = new ResponseContentPart();

        // Use reflection to set private fields (since ResponseContentPart doesn't have setters)
        try {
            var typeField = ResponseContentPart.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(contentPart, "response.content_part.done");

            var itemIdField = ResponseContentPart.class.getDeclaredField("itemId");
            itemIdField.setAccessible(true);
            itemIdField.set(contentPart, "item_xyz789");

            var outputIndexField = ResponseContentPart.class.getDeclaredField("outputIndex");
            outputIndexField.setAccessible(true);
            outputIndexField.set(contentPart, "1");

            var contentIndexField = ResponseContentPart.class.getDeclaredField("contentIndex");
            contentIndexField.setAccessible(true);
            contentIndexField.set(contentPart, "3");

            Map<String, Object> part = new HashMap<>();
            part.put("type", "text");
            part.put("text", "This is a completed text part.");

            var partField = ResponseContentPart.class.getDeclaredField("part");
            partField.setAccessible(true);
            partField.set(contentPart, part);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields on ResponseContentPart", e);
        }

        // Verify
        assertEquals("response.content_part.done", contentPart.getType());
        assertEquals("item_xyz789", contentPart.getItemId());
        assertEquals("1", contentPart.getOutputIndex());
        assertEquals("3", contentPart.getContentIndex());
        assertNotNull(contentPart.getPart());
        assertEquals("text", contentPart.getPart().get("type"));
        assertEquals("This is a completed text part.", contentPart.getPart().get("text"));
    }

    @Test
    void testResponseContentPartSerialization() throws JsonProcessingException {
        // Create a ResponseContentPart by manually setting fields
        ResponseContentPart contentPart = new ResponseContentPart();

        // Use reflection to set private fields
        try {
            var typeField = ResponseContentPart.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(contentPart, "response.content_part.added");

            var itemIdField = ResponseContentPart.class.getDeclaredField("itemId");
            itemIdField.setAccessible(true);
            itemIdField.set(contentPart, "item_test123");

            var outputIndexField = ResponseContentPart.class.getDeclaredField("outputIndex");
            outputIndexField.setAccessible(true);
            outputIndexField.set(contentPart, "0");

            var contentIndexField = ResponseContentPart.class.getDeclaredField("contentIndex");
            contentIndexField.setAccessible(true);
            contentIndexField.set(contentPart, "0");

            Map<String, Object> part = new HashMap<>();
            part.put("type", "text");
            part.put("text", "Test content for serialization.");

            var partField = ResponseContentPart.class.getDeclaredField("part");
            partField.setAccessible(true);
            partField.set(contentPart, part);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields on ResponseContentPart", e);
        }

        // Serialize to JSON
        var json = mapper.writeValueAsString(contentPart);

        // Deserialize back to ResponseContentPart
        var deserializedPart = mapper.readValue(json, ResponseContentPart.class);

        // Verify
        assertEquals("response.content_part.added", deserializedPart.getType());
        assertEquals("item_test123", deserializedPart.getItemId());
        assertEquals("0", deserializedPart.getOutputIndex());
        assertEquals("0", deserializedPart.getContentIndex());
        assertNotNull(deserializedPart.getPart());
        assertEquals("text", deserializedPart.getPart().get("type"));
        assertEquals("Test content for serialization.", deserializedPart.getPart().get("text"));
    }
}