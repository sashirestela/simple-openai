package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.domain.chat.ChatRequest.ReasoningEffort;
import io.github.sashirestela.openai.domain.response.Reasoning.Summary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReasoningTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testReasoningWithEffortOnly() {
        // Create Reasoning with effort only
        var reasoning = Reasoning.of(ReasoningEffort.HIGH);

        // Verify
        assertNotNull(reasoning);
        assertEquals(ReasoningEffort.HIGH, reasoning.getEffort());
        assertNull(reasoning.getSummary());
        assertNotNull(reasoning.toString());
    }

    @Test
    void testReasoningWithEffortAndSummary() {
        // Create Reasoning with both effort and summary
        var reasoning = Reasoning.of(ReasoningEffort.MEDIUM, Summary.DETAILED);

        // Verify
        assertNotNull(reasoning);
        assertEquals(ReasoningEffort.MEDIUM, reasoning.getEffort());
        assertEquals(Summary.DETAILED, reasoning.getSummary());
        assertNotNull(reasoning.toString());
    }

    @Test
    void testReasoningSerialization() throws JsonProcessingException {
        // Create Reasoning with both parameters
        var reasoning = Reasoning.of(ReasoningEffort.LOW, Summary.CONCISE);

        // Serialize to JSON
        var json = mapper.writeValueAsString(reasoning);

        // Deserialize back to Reasoning
        var deserializedReasoning = mapper.readValue(json, Reasoning.class);

        // Verify
        assertNotNull(deserializedReasoning);
        assertEquals(ReasoningEffort.LOW, deserializedReasoning.getEffort());
        assertEquals(Summary.CONCISE, deserializedReasoning.getSummary());
    }

    @Test
    void testReasoningEffortSerialization() throws JsonProcessingException {
        // Test HIGH serialization
        var highJson = mapper.writeValueAsString(ReasoningEffort.HIGH);
        assertEquals("\"high\"", highJson);
        assertEquals(ReasoningEffort.HIGH, mapper.readValue(highJson, ReasoningEffort.class));

        // Test MEDIUM serialization
        var mediumJson = mapper.writeValueAsString(ReasoningEffort.MEDIUM);
        assertEquals("\"medium\"", mediumJson);
        assertEquals(ReasoningEffort.MEDIUM, mapper.readValue(mediumJson, ReasoningEffort.class));

        // Test LOW serialization
        var lowJson = mapper.writeValueAsString(ReasoningEffort.LOW);
        assertEquals("\"low\"", lowJson);
        assertEquals(ReasoningEffort.LOW, mapper.readValue(lowJson, ReasoningEffort.class));
    }

    @Test
    void testSummarySerialization() throws JsonProcessingException {
        // Test AUTO serialization
        var autoJson = mapper.writeValueAsString(Summary.AUTO);
        assertEquals("\"auto\"", autoJson);
        assertEquals(Summary.AUTO, mapper.readValue(autoJson, Summary.class));

        // Test CONCISE serialization
        var conciseJson = mapper.writeValueAsString(Summary.CONCISE);
        assertEquals("\"concise\"", conciseJson);
        assertEquals(Summary.CONCISE, mapper.readValue(conciseJson, Summary.class));

        // Test DETAILED serialization
        var detailedJson = mapper.writeValueAsString(Summary.DETAILED);
        assertEquals("\"detailed\"", detailedJson);
        assertEquals(Summary.DETAILED, mapper.readValue(detailedJson, Summary.class));
    }
}