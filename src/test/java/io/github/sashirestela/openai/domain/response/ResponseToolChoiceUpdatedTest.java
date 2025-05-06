package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.domain.response.ResponseTool.ResponseToolType;
import io.github.sashirestela.openai.domain.response.ResponseToolChoice.FunctionChoice;
import io.github.sashirestela.openai.domain.response.ResponseToolChoice.HostedTool;
import io.github.sashirestela.openai.domain.response.ResponseToolChoice.HostedToolType;
import io.github.sashirestela.openai.domain.response.ResponseToolChoice.ToolChoiceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Updated and comprehensive tests for ResponseToolChoice class, including all nested classes and
 * related functionality.
 */
class ResponseToolChoiceUpdatedTest {

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

        // Test "function" representation
        assertEquals("function", ResponseToolChoice.of(ToolChoiceType.FUNCTION));

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

    /* Additional tests for complete coverage */

    @Test
    void testToolChoiceTypeToString() {
        // Test toString implementation of each enum value directly
        assertEquals("auto", ToolChoiceType.AUTO.toString());
        assertEquals("function", ToolChoiceType.FUNCTION.toString());
        assertEquals("none", ToolChoiceType.NONE.toString());
        assertEquals("required", ToolChoiceType.REQUIRED.toString());

        // The toString() method has a custom implementation that uses hardcoded values
        // which happens to match name().toLowerCase() for some values, but not for conceptual reasons
        // Verify we've covered all enum values
        assertEquals(4, ToolChoiceType.values().length,
                "This test should be updated if new enum values are added");
    }

    @Test
    void testHostedToolConstants() {
        // Test static constants for hosted tools
        assertNotNull(HostedTool.FILE_SEARCH);
        assertEquals(HostedToolType.FILE_SEARCH, HostedTool.FILE_SEARCH.getType());

        assertNotNull(HostedTool.WEB_SEARCH_PREVIEW);
        assertEquals(HostedToolType.WEB_SEARCH_PREVIEW, HostedTool.WEB_SEARCH_PREVIEW.getType());

        // Test toString method
        assertNotNull(HostedTool.FILE_SEARCH.toString());
        assertTrue(HostedTool.FILE_SEARCH.toString().contains("FILE_SEARCH"));
        assertNotNull(HostedTool.WEB_SEARCH_PREVIEW.toString());
        assertTrue(HostedTool.WEB_SEARCH_PREVIEW.toString().contains("WEB_SEARCH_PREVIEW"));
    }

    @Test
    void testHostedToolTypeJsonPropertyMapping() throws JsonProcessingException {
        // Test JSON property mapping for HostedToolType
        var fileSearchJson = mapper.writeValueAsString(HostedToolType.FILE_SEARCH);
        assertEquals("\"file_search\"", fileSearchJson);

        var webSearchJson = mapper.writeValueAsString(HostedToolType.WEB_SEARCH_PREVIEW);
        assertEquals("\"web_search_preview\"", webSearchJson);

        // Test deserialization
        assertEquals(HostedToolType.FILE_SEARCH, mapper.readValue("\"file_search\"", HostedToolType.class));
        assertEquals(HostedToolType.WEB_SEARCH_PREVIEW,
                mapper.readValue("\"web_search_preview\"", HostedToolType.class));
    }

    @Test
    void testHostedToolSerialization() throws JsonProcessingException {
        // Test FILE_SEARCH serialization
        var fileSearchJson = mapper.writeValueAsString(HostedTool.FILE_SEARCH);
        assertEquals("{\"type\":\"file_search\"}", fileSearchJson);

        // Test deserialization
        var fileSearch = mapper.readValue(fileSearchJson, HostedTool.class);
        assertEquals(HostedToolType.FILE_SEARCH, fileSearch.getType());

        // Test WEB_SEARCH_PREVIEW serialization
        var webSearchJson = mapper.writeValueAsString(HostedTool.WEB_SEARCH_PREVIEW);
        assertEquals("{\"type\":\"web_search_preview\"}", webSearchJson);

        // Test deserialization
        var webSearch = mapper.readValue(webSearchJson, HostedTool.class);
        assertEquals(HostedToolType.WEB_SEARCH_PREVIEW, webSearch.getType());
    }

    @Test
    void testHostedToolManualCreation() {
        // Test manual creation of HostedTool (empty constructor)
        var emptyTool = new HostedTool();
        assertNull(emptyTool.getType());

        // Setter test (use reflection to set the private field)
        try {
            var typeField = HostedTool.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(emptyTool, HostedToolType.FILE_SEARCH);
            assertEquals(HostedToolType.FILE_SEARCH, emptyTool.getType());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field", e);
        }
    }

    @Test
    void testFunctionWithNullName() {
        // Test function with null name
        var toolChoice = ResponseToolChoice.function(null);

        // Verify
        assertEquals(ResponseToolType.FUNCTION, toolChoice.getType());
        assertNotNull(toolChoice.getFunction());
        assertNull(toolChoice.getFunction().getName());
    }

    @Test
    void testFunctionWithEmptyName() {
        // Test function with empty name
        var toolChoice = ResponseToolChoice.function("");

        // Verify
        assertEquals(ResponseToolType.FUNCTION, toolChoice.getType());
        assertNotNull(toolChoice.getFunction());
        assertEquals("", toolChoice.getFunction().getName());
    }

    @Test
    void testEmptyBuilder() {
        // Test builder with minimal values
        var toolChoice = ResponseToolChoice.builder().build();

        // Verify
        assertNull(toolChoice.getType());
        assertNull(toolChoice.getFunction());
    }

    @Test
    void testResponseRequestWithHostedTool() throws JsonProcessingException {
        // Create a request with web search tool as part of ResponseToolChoice
        var request = ResponseRequest.builder()
                .input("What's the most recent news about climate change?")
                .model("gpt-4o")
                .tool(ResponseTool.webSearchTool())
                .toolChoice(ResponseToolChoice.builder()
                        .type(ResponseToolType.WEB_SEARCH_PREVIEW)
                        .build())
                .build();

        // Serialize to JSON
        var json = mapper.writeValueAsString(request);

        // Verify it contains the web search tool choice
        assertTrue(json.contains("\"type\":\"web_search_preview\""));
        assertTrue(json.contains("\"tool_choice\":{\"type\":\"web_search_preview\"}"));
    }

    @Test
    void testObjectEquality() {
        // Test equals/hashCode for FunctionChoice
        var function1 = FunctionChoice.builder().name("same").build();
        var function2 = FunctionChoice.builder().name("same").build();
        var function3 = FunctionChoice.builder().name("different").build();

        assertEquals(function1, function2);
        assertEquals(function1.hashCode(), function2.hashCode());
        assertFalse(function1.equals(function3));
        assertFalse(function1.equals(null));
        assertTrue(function1.equals(function1)); // reflective

        // Test toString() method
        assertNotNull(function1.toString());
        assertTrue(function1.toString().contains("same"));

        // Test ResponseToolChoice equals/hashCode
        var choice1 = new ResponseToolChoice(ResponseToolType.FUNCTION, function1);
        var choice2 = new ResponseToolChoice(ResponseToolType.FUNCTION, function1);
        var choice3 = new ResponseToolChoice(ResponseToolType.FUNCTION, function3);
        var choice4 = new ResponseToolChoice(ResponseToolType.WEB_SEARCH_PREVIEW, null);

        assertEquals(choice1, choice2);
        assertEquals(choice1.hashCode(), choice2.hashCode());
        assertFalse(choice1.equals(choice3));
        assertFalse(choice1.equals(choice4));
        assertFalse(choice1.equals(null));
        assertTrue(choice1.equals(choice1)); // reflective

        // Test ResponseToolChoice toString()
        assertNotNull(choice1.toString());
        assertTrue(choice1.toString().contains("FUNCTION"));
    }

    @Test
    void testMultipleToolBuilderUsage() {
        // Test building with empty function first
        var toolChoice = ResponseToolChoice.builder()
                .function(new FunctionChoice())
                .type(ResponseToolType.FUNCTION)
                .function(FunctionChoice.builder().name("updated").build())
                .build();

        // Verify the second function call took precedence
        assertEquals("updated", toolChoice.getFunction().getName());
    }

    @Test
    void testConstructors() {
        // Test AllArgsConstructor
        var fc = new FunctionChoice("test-function");
        var rtc = new ResponseToolChoice(ResponseToolType.FUNCTION, fc);

        assertEquals(ResponseToolType.FUNCTION, rtc.getType());
        assertEquals("test-function", rtc.getFunction().getName());

        // Test NoArgsConstructor
        var empty = new ResponseToolChoice();
        assertNull(empty.getType());
        assertNull(empty.getFunction());

        // Test FunctionChoice constructors
        var fc1 = new FunctionChoice("name1");
        assertEquals("name1", fc1.getName());

        var fc2 = new FunctionChoice();
        assertNull(fc2.getName());

        // Test name setter
        fc2.setName("name2");
        assertEquals("name2", fc2.getName());
    }

}
