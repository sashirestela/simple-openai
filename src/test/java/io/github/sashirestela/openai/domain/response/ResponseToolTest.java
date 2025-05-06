package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.domain.response.ResponseTool.ResponseToolType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseToolTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testFunctionToolCreation() {
        // Create parameters for the function
        var parameters = mapper.createObjectNode();
        var properties = mapper.createObjectNode();

        var locationProp = mapper.createObjectNode();
        locationProp.put("type", "string");
        locationProp.put("description", "The city and state, e.g. San Francisco, CA");

        properties.set("location", locationProp);
        parameters.set("properties", properties);
        parameters.put("type", "object");
        parameters.set("required", mapper.createArrayNode().add("location"));

        // Create tool using the direct builder approach
        var toolWithBuilder = ResponseTool.builder()
                .type(ResponseToolType.FUNCTION)
                .name("get_weather")
                .description("Get the current weather in a given location")
                .parameters(parameters)
                .strict(true)
                .build();

        // Create tool using the convenience method
        var toolWithMethod = ResponseTool.functionTool(
                "get_weather",
                "Get the current weather in a given location",
                parameters);

        // Create tool with strict parameter
        var toolWithStrict = ResponseTool.functionTool(
                "get_weather",
                "Get the current weather in a given location",
                parameters,
                true);

        // Verify builder created correctly
        assertEquals(ResponseToolType.FUNCTION, toolWithBuilder.getType());
        assertEquals("get_weather", toolWithBuilder.getName());
        assertEquals("Get the current weather in a given location", toolWithBuilder.getDescription());
        assertNotNull(toolWithBuilder.getParameters());
        assertEquals(true, toolWithBuilder.getStrict());

        // Verify convenience method created correctly
        assertEquals(ResponseToolType.FUNCTION, toolWithMethod.getType());
        assertEquals("get_weather", toolWithMethod.getName());
        assertEquals("Get the current weather in a given location", toolWithMethod.getDescription());
        assertNotNull(toolWithMethod.getParameters());
        assertNull(toolWithMethod.getStrict()); // Default is null

        // Verify strict parameter
        assertEquals(true, toolWithStrict.getStrict());
    }

    @Test
    void testToolSerialization() throws JsonProcessingException {
        // Create simple function tool
        var parameters = mapper.readTree("{\"type\":\"object\",\"properties\":{\"query\":{\"type\":\"string\"}}}");

        var tool = ResponseTool.functionTool(
                "search",
                "Search for information",
                parameters,
                true);

        // Serialize to JSON
        var json = mapper.writeValueAsString(tool);

        // Deserialize back
        var result = mapper.readValue(json, ResponseTool.class);

        // Verify
        assertEquals(ResponseToolType.FUNCTION, result.getType());
        assertEquals("search", result.getName());
        assertEquals("Search for information", result.getDescription());
        assertNotNull(result.getParameters());
        assertEquals(true, result.getStrict());

        // Verify JSON format matches the API expectation
        assertTrue(json.contains("\"type\":\"function\""));
        assertTrue(json.contains("\"name\":\"search\""));
        assertTrue(json.contains("\"description\":\"Search for information\""));
        assertTrue(json.contains("\"strict\":true"));
    }

    @Test
    void testBuiltInTools() throws JsonProcessingException {
        // Create built-in tools
        var webSearchTool = ResponseTool.webSearchTool();
        var fileSearchTool = ResponseTool.fileSearchTool(List.of());

        // Verify web search tool
        assertEquals(ResponseToolType.WEB_SEARCH_PREVIEW, webSearchTool.getType());
        assertNull(webSearchTool.getName());
        assertNull(webSearchTool.getDescription());
        assertNull(webSearchTool.getParameters());

        // Verify file search tool
        assertEquals(ResponseToolType.FILE_SEARCH, fileSearchTool.getType());
        assertNull(fileSearchTool.getName());
        assertNull(fileSearchTool.getDescription());
        assertNull(fileSearchTool.getParameters());

        // Test serialization of built-in tools
        var webSearchJson = mapper.writeValueAsString(webSearchTool);
        assertEquals("{\"type\":\"web_search_preview\"}", webSearchJson);

        var fileSearchJson = mapper.writeValueAsString(fileSearchTool);
        assertEquals("{\"type\":\"file_search\",\"vector_store_ids\":[]}", fileSearchJson);
    }

    @Test
    void testBuiltInToolRequest() throws JsonProcessingException {
        // Create a request with the web search tool
        var request = ResponseRequest.builder()
                .input("What was a positive news story from today?")
                .model("gpt-4o")
                .tool(ResponseTool.webSearchTool())
                .build();

        // Serialize to JSON
        var json = mapper.writeValueAsString(request);

        // Verify it contains the web search tool
        assertTrue(json.contains("\"type\":\"web_search_preview\""));
    }

}
