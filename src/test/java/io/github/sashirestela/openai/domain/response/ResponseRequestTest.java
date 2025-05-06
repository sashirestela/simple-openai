package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.domain.chat.ChatRequest.ReasoningEffort;
import io.github.sashirestela.openai.domain.response.Input.MessageRole;
import io.github.sashirestela.openai.domain.response.Reasoning.Summary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseRequestTest {

    @Test
    void testResponseRequestSerialization() throws JsonProcessingException {
        // Create a ResponseRequest object
        var request = ResponseRequest.builder()
                .input(Input.InputMessage.of("What is the capital of France?", MessageRole.USER))
                .model("gpt-4o")
                .instructions("You are a helpful assistant.")
                .maxOutputTokens(1000)
                .reasoning(Reasoning.of(ReasoningEffort.HIGH, Summary.CONCISE))
                .stream(true)
                .temperature(0.7)
                .build();

        // Serialize to JSON
        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(request);

        // Deserialize back to ResponseRequest
        var deserializedRequest = mapper.readValue(json, ResponseRequest.class);

        // Verify
        assertNotNull(deserializedRequest);
        assertEquals("gpt-4o", deserializedRequest.getModel());
        assertEquals("You are a helpful assistant.", deserializedRequest.getInstructions());
        assertEquals(1000, deserializedRequest.getMaxOutputTokens());
        assertEquals(true, deserializedRequest.getStream());
        assertEquals(0.7, deserializedRequest.getTemperature());
    }

}
