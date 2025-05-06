package io.github.sashirestela.openai.domain.response.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.domain.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseBaseEventTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testResponseBaseEventCreation() {
        // Create a ResponseBaseEvent by deserialization
        String json = "{\n" +
            "  \"type\": \"response.created\",\n" +
            "  \"response\": {\n" +
            "    \"id\": \"resp_abc123\",\n" +
            "    \"model\": \"gpt-4o\",\n" +
            "    \"object\": \"response\",\n" +
            "    \"created_at\": 1234567890,\n" +
            "    \"items\": []\n" +
            "  }\n" +
            "}";

        try {
            // Deserialize
            ResponseBaseEvent event = mapper.readValue(json, ResponseBaseEvent.class);

            // Verify
            assertNotNull(event);
            assertEquals("response.created", event.getType());
            assertNotNull(event.getResponse());
            assertEquals("resp_abc123", event.getResponse().getId());
            assertEquals("gpt-4o", event.getResponse().getModel());
            assertEquals("response", event.getResponse().getObject());
            assertEquals(1234567890L, event.getResponse().getCreated());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize ResponseBaseEvent", e);
        }
    }

    @Test
    void testResponseBaseEventWithFullResponse() {
        // Create a Response object
        var response = Response.builder()
                .id("resp_123456")
                .model("gpt-4o")
                .object("response")
                .created(1234567890L)
                .instructions("You are a helpful assistant.")
                .items(new ArrayList<>())
                .metadata(new HashMap<>())
                .usage(Response.Usage.builder()
                        .promptTokens(10)
                        .completionTokens(15)
                        .totalTokens(25)
                        .build())
                .build();

        // Create an event with manually set fields
        ResponseBaseEvent event = new ResponseBaseEvent();

        // Use reflection to set private fields (since ResponseBaseEvent doesn't have setters)
        try {
            var typeField = ResponseBaseEvent.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(event, "response.completed");

            var responseField = ResponseBaseEvent.class.getDeclaredField("response");
            responseField.setAccessible(true);
            responseField.set(event, response);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields on ResponseBaseEvent", e);
        }

        // Verify
        assertEquals("response.completed", event.getType());
        assertNotNull(event.getResponse());
        assertEquals("resp_123456", event.getResponse().getId());
        assertEquals("gpt-4o", event.getResponse().getModel());
        assertEquals(10, event.getResponse().getUsage().getPromptTokens());
        assertEquals(15, event.getResponse().getUsage().getCompletionTokens());
        assertEquals(25, event.getResponse().getUsage().getTotalTokens());
    }

    @Test
    void testResponseBaseEventSerialization() throws JsonProcessingException {
        // Create a Response object
        var response = Response.builder()
                .id("resp_xyz789")
                .model("gpt-4o")
                .object("response")
                .created(1234567890L)
                .build();

        // Create an event with manually set fields
        ResponseBaseEvent event = new ResponseBaseEvent();

        // Use reflection to set private fields
        try {
            var typeField = ResponseBaseEvent.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(event, "response.in_progress");

            var responseField = ResponseBaseEvent.class.getDeclaredField("response");
            responseField.setAccessible(true);
            responseField.set(event, response);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields on ResponseBaseEvent", e);
        }

        // Serialize to JSON
        var json = mapper.writeValueAsString(event);

        // Deserialize back to ResponseBaseEvent
        var deserializedEvent = mapper.readValue(json, ResponseBaseEvent.class);

        // Verify
        assertEquals("response.in_progress", deserializedEvent.getType());
        assertNotNull(deserializedEvent.getResponse());
        assertEquals("resp_xyz789", deserializedEvent.getResponse().getId());
        assertEquals("gpt-4o", deserializedEvent.getResponse().getModel());
    }

    @Test
    void testWithErrorResponse() {
        // Create a Response object with an error
        var response = Response.builder()
                .id("resp_error123")
                .error(Response.Error.builder()
                        .code("rate_limit_exceeded")
                        .message("You have exceeded your rate limit")
                        .param("model")
                        .type("invalid_request_error")
                        .build())
                .build();

        // Create an event with manually set fields
        ResponseBaseEvent event = new ResponseBaseEvent();

        // Use reflection to set private fields
        try {
            var typeField = ResponseBaseEvent.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(event, "error");

            var responseField = ResponseBaseEvent.class.getDeclaredField("response");
            responseField.setAccessible(true);
            responseField.set(event, response);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields on ResponseBaseEvent", e);
        }

        // Verify
        assertEquals("error", event.getType());
        assertNotNull(event.getResponse());
        assertNotNull(event.getResponse().getError());
        assertEquals("rate_limit_exceeded", event.getResponse().getError().getCode());
        assertEquals("You have exceeded your rate limit", event.getResponse().getError().getMessage());
        assertEquals("model", event.getResponse().getError().getParam());
        assertEquals("invalid_request_error", event.getResponse().getError().getType());
    }
}