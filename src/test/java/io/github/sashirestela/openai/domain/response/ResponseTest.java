package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testResponseSerialization() throws JsonProcessingException {
        // Create a Response object with all fields
        var items = new ArrayList<>();
        var textItem = new HashMap<String, Object>();
        textItem.put("id", "item_abc123");
        textItem.put("type", "text");

        var textContent = new HashMap<String, Object>();
        textContent.put("value", "Paris is the capital of France.");
        textContent.put("annotations", new ArrayList<>());

        textItem.put("text", textContent);
        items.add(textItem);

        var response = Response.builder()
                .id("resp_abc123")
                .model("gpt-4o")
                .object("response")
                .created(1234567890L)
                .instructions("You are a helpful assistant.")
                .items(items)
                .metadata(new HashMap<>())
                .usage(Response.Usage.builder()
                        .promptTokens(10)
                        .completionTokens(7)
                        .totalTokens(17)
                        .build())
                .build();

        // Serialize to JSON
        var json = mapper.writeValueAsString(response);

        // Deserialize back to Response
        var deserializedResponse = mapper.readValue(json, Response.class);

        // Verify
        assertNotNull(deserializedResponse);
        assertEquals("resp_abc123", deserializedResponse.getId());
        assertEquals("gpt-4o", deserializedResponse.getModel());
        assertEquals("response", deserializedResponse.getObject());
        assertEquals(1234567890L, deserializedResponse.getCreated());
        assertEquals("You are a helpful assistant.", deserializedResponse.getInstructions());
        assertEquals(1, deserializedResponse.getItems().size());
        assertEquals(10, deserializedResponse.getUsage().getPromptTokens());
        assertEquals(7, deserializedResponse.getUsage().getCompletionTokens());
        assertEquals(17, deserializedResponse.getUsage().getTotalTokens());
    }

    @Test
    void testResponseWithErrorSerialization() throws JsonProcessingException {
        // Create a Response object with error
        var response = Response.builder()
                .id("resp_abc123")
                .model("gpt-4o")
                .object("response")
                .created(1234567890L)
                .error(Response.Error.builder()
                        .code("rate_limit_exceeded")
                        .message("You have exceeded your rate limit")
                        .param("model")
                        .type("invalid_request_error")
                        .build())
                .build();

        // Serialize to JSON
        var json = mapper.writeValueAsString(response);

        // Deserialize back to Response
        var deserializedResponse = mapper.readValue(json, Response.class);

        // Verify
        assertNotNull(deserializedResponse);
        assertNotNull(deserializedResponse.getError());
        assertEquals("rate_limit_exceeded", deserializedResponse.getError().getCode());
        assertEquals("You have exceeded your rate limit", deserializedResponse.getError().getMessage());
        assertEquals("model", deserializedResponse.getError().getParam());
        assertEquals("invalid_request_error", deserializedResponse.getError().getType());
    }

    /* New tests for IncompleteDetails inner class */

    @Test
    void testResponseWithIncompleteDetailsContentFilter() throws JsonProcessingException {
        // Create a Response object with incomplete details (content filter)
        var response = Response.builder()
                .id("resp_abc123")
                .model("gpt-4o")
                .object("response")
                .created(1234567890L)
                .incompleteDetails(Response.IncompleteDetails.builder()
                        .reason("content_filter")
                        .build())
                .build();

        // Serialize to JSON
        var json = mapper.writeValueAsString(response);

        // Check JSON structure
        assertTrue(json.contains("\"incomplete_details\":{\"reason\":\"content_filter\"}"));

        // Deserialize back to Response
        var deserializedResponse = mapper.readValue(json, Response.class);

        // Verify
        assertNotNull(deserializedResponse);
        assertNotNull(deserializedResponse.getIncompleteDetails());
        assertEquals("content_filter", deserializedResponse.getIncompleteDetails().getReason());
    }

    @Test
    void testResponseWithIncompleteDetailsMaxTokens() throws JsonProcessingException {
        // Create a Response object with incomplete details (max tokens)
        var response = Response.builder()
                .id("resp_abc123")
                .model("gpt-4o")
                .object("response")
                .created(1234567890L)
                .incompleteDetails(Response.IncompleteDetails.builder()
                        .reason("max_tokens")
                        .build())
                .build();

        // Serialize to JSON
        var json = mapper.writeValueAsString(response);

        // Check JSON structure
        assertTrue(json.contains("\"incomplete_details\":{\"reason\":\"max_tokens\"}"));

        // Deserialize back to Response
        var deserializedResponse = mapper.readValue(json, Response.class);

        // Verify
        assertNotNull(deserializedResponse);
        assertNotNull(deserializedResponse.getIncompleteDetails());
        assertEquals("max_tokens", deserializedResponse.getIncompleteDetails().getReason());
    }

    @Test
    void testResponseWithIncompleteDetailsTokenLimit() throws JsonProcessingException {
        // Create a Response object with incomplete details (token limit)
        var response = Response.builder()
                .id("resp_abc123")
                .model("gpt-4o")
                .object("response")
                .created(1234567890L)
                .incompleteDetails(Response.IncompleteDetails.builder()
                        .reason("token_limit")
                        .build())
                .build();

        // Serialize to JSON
        var json = mapper.writeValueAsString(response);

        // Check JSON structure
        assertTrue(json.contains("\"incomplete_details\":{\"reason\":\"token_limit\"}"));

        // Deserialize back to Response
        var deserializedResponse = mapper.readValue(json, Response.class);

        // Verify
        assertNotNull(deserializedResponse);
        assertNotNull(deserializedResponse.getIncompleteDetails());
        assertEquals("token_limit", deserializedResponse.getIncompleteDetails().getReason());
    }

    @Test
    void testIncompleteDetailsClassDirectly() throws JsonProcessingException {
        // Test the builder
        var details = Response.IncompleteDetails.builder()
                .reason("custom_reason")
                .build();

        assertEquals("custom_reason", details.getReason());

        // Test serialization
        var json = mapper.writeValueAsString(details);
        assertEquals("{\"reason\":\"custom_reason\"}", json);

        // Test deserialization
        var deserializedDetails = mapper.readValue(json, Response.IncompleteDetails.class);
        assertEquals("custom_reason", deserializedDetails.getReason());

        // Test empty constructor
        var emptyDetails = new Response.IncompleteDetails();
        assertNull(emptyDetails.getReason());

        // Test all args constructor
        var allArgsDetails = new Response.IncompleteDetails("another_reason");
        assertEquals("another_reason", allArgsDetails.getReason());

        // Test toString
        var toStringResult = details.toString();
        assertTrue(toStringResult.contains("reason=custom_reason"));
    }

    @Test
    void testIncompleteDetailsReasonEnum() throws JsonProcessingException {
        // Test JSON serialization of enum values
        var contentFilterJson = mapper.writeValueAsString(Response.IncompleteDetails.Reason.CONTENT_FILTER);
        assertEquals("\"content_filter\"", contentFilterJson);

        var maxTokensJson = mapper.writeValueAsString(Response.IncompleteDetails.Reason.MAX_TOKENS);
        assertEquals("\"max_tokens\"", maxTokensJson);

        var tokenLimitJson = mapper.writeValueAsString(Response.IncompleteDetails.Reason.TOKEN_LIMIT);
        assertEquals("\"token_limit\"", tokenLimitJson);

        // Test deserialization
        assertEquals(Response.IncompleteDetails.Reason.CONTENT_FILTER,
                mapper.readValue("\"content_filter\"", Response.IncompleteDetails.Reason.class));
        assertEquals(Response.IncompleteDetails.Reason.MAX_TOKENS,
                mapper.readValue("\"max_tokens\"", Response.IncompleteDetails.Reason.class));
        assertEquals(Response.IncompleteDetails.Reason.TOKEN_LIMIT,
                mapper.readValue("\"token_limit\"", Response.IncompleteDetails.Reason.class));
    }

    @Test
    void testResponseDeserializationWithIncompleteDetails() throws JsonProcessingException {
        // Create a JSON string with incomplete details
        String json = "{\n" +
                "  \"id\": \"resp_abc123\",\n" +
                "  \"model\": \"gpt-4o\",\n" +
                "  \"object\": \"response\",\n" +
                "  \"created_at\": 1234567890,\n" +
                "  \"incomplete_details\": {\n" +
                "    \"reason\": \"token_limit\"\n" +
                "  }\n" +
                "}";

        // Deserialize to Response
        var response = mapper.readValue(json, Response.class);

        // Verify
        assertNotNull(response);
        assertEquals("resp_abc123", response.getId());
        assertEquals("gpt-4o", response.getModel());
        assertNotNull(response.getIncompleteDetails());
        assertEquals("token_limit", response.getIncompleteDetails().getReason());
    }

    @Test
    void testCompleteResponseWithAllFields() throws JsonProcessingException {
        // Create a Response object with all fields including IncompleteDetails
        var items = new ArrayList<>();
        var textItem = new HashMap<String, Object>();
        textItem.put("id", "item_abc123");
        textItem.put("type", "text");

        var textContent = new HashMap<String, Object>();
        textContent.put("value", "Paris is the capital of France.");
        textContent.put("annotations", new ArrayList<>());

        textItem.put("text", textContent);
        items.add(textItem);

        var outputItems = new ArrayList<>();
        outputItems.add("Sample output");

        var response = Response.builder()
                .id("resp_abc123")
                .model("gpt-4o")
                .object("response")
                .created(1234567890L)
                .instructions("You are a helpful assistant.")
                .items(items)
                .output(outputItems)
                .previousResponseId("resp_previous")
                .user("user_123")
                .metadata(new HashMap<>())
                .usage(Response.Usage.builder()
                        .promptTokens(10)
                        .completionTokens(7)
                        .totalTokens(17)
                        .build())
                .error(Response.Error.builder()
                        .code("warning_only")
                        .message("This is just a warning")
                        .build())
                .incompleteDetails(Response.IncompleteDetails.builder()
                        .reason("max_tokens")
                        .build())
                .build();

        // Serialize to JSON
        var json = mapper.writeValueAsString(response);

        // Deserialize back to Response
        var deserializedResponse = mapper.readValue(json, Response.class);

        // Verify all fields
        assertNotNull(deserializedResponse);
        assertEquals("resp_abc123", deserializedResponse.getId());
        assertEquals("gpt-4o", deserializedResponse.getModel());
        assertEquals("response", deserializedResponse.getObject());
        assertEquals(1234567890L, deserializedResponse.getCreated());
        assertEquals("You are a helpful assistant.", deserializedResponse.getInstructions());
        assertEquals(1, deserializedResponse.getItems().size());
        assertEquals(1, deserializedResponse.getOutput().size());
        assertEquals("resp_previous", deserializedResponse.getPreviousResponseId());
        assertEquals("user_123", deserializedResponse.getUser());
        assertNotNull(deserializedResponse.getMetadata());

        // Verify usage
        assertEquals(10, deserializedResponse.getUsage().getPromptTokens());
        assertEquals(7, deserializedResponse.getUsage().getCompletionTokens());
        assertEquals(17, deserializedResponse.getUsage().getTotalTokens());

        // Verify error
        assertNotNull(deserializedResponse.getError());
        assertEquals("warning_only", deserializedResponse.getError().getCode());
        assertEquals("This is just a warning", deserializedResponse.getError().getMessage());

        // Verify incomplete details
        assertNotNull(deserializedResponse.getIncompleteDetails());
        assertEquals("max_tokens", deserializedResponse.getIncompleteDetails().getReason());
    }

}
