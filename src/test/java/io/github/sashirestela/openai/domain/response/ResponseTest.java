package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseTest {

    @Test
    void testResponseSerialization() throws JsonProcessingException {
        // Create a Response object with all fields
        var items = new ArrayList<Object>();
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
        var mapper = new ObjectMapper();
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
        var mapper = new ObjectMapper();
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
}
