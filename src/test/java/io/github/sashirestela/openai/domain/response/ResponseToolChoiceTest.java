package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.domain.response.ResponseTool.ResponseToolType;
import io.github.sashirestela.openai.domain.response.ResponseToolChoice.FunctionChoice;
import io.github.sashirestela.openai.domain.response.ResponseToolChoice.ToolChoiceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ResponseToolChoiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testToolChoiceStringValues() {
        // Test "auto" representation
        assertEquals("auto", ResponseToolChoice.auto());
        assertEquals("auto", ResponseToolChoice.of(ToolChoiceType.AUTO));
        
        // Test "none" representation
        assertEquals("none", ResponseToolChoice.none());
        assertEquals("none", ResponseToolChoice.of(ToolChoiceType.NONE));
        
        // Test "required" representation
        assertEquals("required", ResponseToolChoice.required());
        assertEquals("required", ResponseToolChoice.of(ToolChoiceType.REQUIRED));
        
        // Test custom string value
        assertEquals("custom", ResponseToolChoice.of("custom"));
    }

    @Test
    void testFunctionToolChoice() {
        // Create a function tool choice
        var toolChoice = ResponseToolChoice.function("multiply");
        
        // Verify
        assertEquals(ResponseToolType.FUNCTION, toolChoice.getType());
        assertNotNull(toolChoice.getFunction());
        assertEquals("multiply", toolChoice.getFunction().getName());
    }
    
    @Test
    void testBuilderPattern() {
        // Create with builder
        var toolChoice = ResponseToolChoice.builder()
            .type(ResponseToolType.FUNCTION)
            .function(FunctionChoice.builder().name("add").build())
            .build();
        
        // Verify
        assertEquals(ResponseToolType.FUNCTION, toolChoice.getType());
        assertNotNull(toolChoice.getFunction());
        assertEquals("add", toolChoice.getFunction().getName());
    }
    
    @Test
    void testToolChoiceTypeSerialization() throws JsonProcessingException {
        // Test AUTO serialization
        var autoJson = mapper.writeValueAsString(ToolChoiceType.AUTO);
        assertEquals("\"auto\"", autoJson);
        assertEquals(ToolChoiceType.AUTO, mapper.readValue(autoJson, ToolChoiceType.class));
        
        // Test REQUIRED serialization
        var requiredJson = mapper.writeValueAsString(ToolChoiceType.REQUIRED);
        assertEquals("\"required\"", requiredJson);
        assertEquals(ToolChoiceType.REQUIRED, mapper.readValue(requiredJson, ToolChoiceType.class));
        
        // Test NONE serialization
        var noneJson = mapper.writeValueAsString(ToolChoiceType.NONE);
        assertEquals("\"none\"", noneJson);
        assertEquals(ToolChoiceType.NONE, mapper.readValue(noneJson, ToolChoiceType.class));
        
        // Test FUNCTION serialization
        var functionJson = mapper.writeValueAsString(ToolChoiceType.FUNCTION);
        assertEquals("\"function\"", functionJson);
        assertEquals(ToolChoiceType.FUNCTION, mapper.readValue(functionJson, ToolChoiceType.class));
    }
    
    @Test
    void testFunctionChoiceSerialization() throws JsonProcessingException {
        // Create and serialize
        var choice = FunctionChoice.builder().name("calculate").build();
        var json = mapper.writeValueAsString(choice);
        
        // Verify contains name field
        assertEquals("{\"name\":\"calculate\"}", json);
        
        // Deserialize back
        var result = mapper.readValue(json, FunctionChoice.class);
        
        // Verify
        assertEquals("calculate", result.getName());
    }
    
    @Test
    void testCompleteToolChoiceSerialization() throws JsonProcessingException {
        // Create complete tool choice
        var toolChoice = ResponseToolChoice.builder()
            .type(ResponseToolType.FUNCTION)
            .function(FunctionChoice.builder().name("divide").build())
            .build();
        
        // Serialize
        var json = mapper.writeValueAsString(toolChoice);
        
        // Deserialize back
        var result = mapper.readValue(json, ResponseToolChoice.class);
        
        // Verify
        assertEquals(ResponseToolType.FUNCTION, result.getType());
        assertNotNull(result.getFunction());
        assertEquals("divide", result.getFunction().getName());
    }
}

