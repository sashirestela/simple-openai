package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * Represents a tool definition for the Response API.
 *
 * Tools allow the model to perform specific actions. The Response API has a different
 * structure for function tools than the Chat Completions API:
 * - Function parameters are defined directly at the top level
 * - Each tool has a type (function, file_search, web_search_preview)
 * - No nested "function" object is used (unlike Chat Completions API)
 *
 * Example usage for function tools:
 * ```java
 * // Using builder
 * ResponseTool tool = ResponseTool.builder()
 *     .type(ResponseToolType.FUNCTION)
 *     .name("get_weather")
 *     .description("Get the current weather for a location")
 *     .parameters(schema)
 *     .strict(true)
 *     .build();
 *
 * // Using convenience method
 * ResponseTool tool = ResponseTool.functionTool(
 *     "get_weather",
 *     "Get the current weather for a location",
 *     schema
 * );
 * ```
 *
 * Example for built-in tools:
 * ```java
 * // Web search tool
 * ResponseTool webSearch = ResponseTool.webSearchTool();
 *
 * // File search tool
 * ResponseTool fileSearch = ResponseTool.fileSearchTool();
 * ```
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseTool {

    /**
     * The type of tool, such as "function", "file_search", or "web_search_preview".
     */
    private ResponseToolType type;

    /**
     * The name of the function (only required for function tools).
     */
    private String name;

    /**
     * A description of what the function does (only for function tools).
     */
    private String description;

    /**
     * The JSON Schema object defining the function's parameters (only for function tools).
     */
    private JsonNode parameters;

    /**
     * Whether to validate the function arguments against the schema (only for function tools).
     */
    private Boolean strict;

    /**
     * The IDs of the vector stores to use for file search.
     */
    Collection<String > vectorStoreIds;

    /**
     * The types of tools that can be used with the Response API.
     */
    public enum ResponseToolType {

        /**
         * Allows the model to search through files uploaded by the user.
         */
        @JsonProperty("file_search")
        FILE_SEARCH,

        /**
         * Allows the model to call a function defined by the developer.
         */
        @JsonProperty("function")
        FUNCTION,

        /**
         * Gives the model the ability to search the web.
         * Currently available in the Response API.
         */
        @JsonProperty("web_search_preview")
        WEB_SEARCH_PREVIEW;

        // Note: Code interpreter (computer_use_preview) is not yet available in the Response API
    }

    // Note: The Response API function format does not use a nested function object structure.
    // Instead, it uses top-level fields for name, description, and parameters.

    /**
     * Creates a new function tool for the Response API.
     *
     * @param name The name of the function
     * @param description A description of what the function does
     * @param parameters The JSON Schema object defining the function's parameters
     * @param strict Whether to validate the function arguments against the schema
     * @return A new ResponseTool configured as a function
     */
    public static ResponseTool functionTool(String name, String description, JsonNode parameters, Boolean strict) {
        return ResponseTool.builder()
            .type(ResponseToolType.FUNCTION)
            .name(name)
            .description(description)
            .parameters(parameters)
            .strict(strict)
            .build();
    }

    /**
     * Creates a new function tool for the Response API.
     * Default strict value is not set (null).
     *
     * @param name The name of the function
     * @param description A description of what the function does
     * @param parameters The JSON Schema object defining the function's parameters
     * @return A new ResponseTool configured as a function
     */
    public static ResponseTool functionTool(String name, String description, JsonNode parameters) {
        return functionTool(name, description, parameters, null);
    }

    /**
     * Creates a web search tool for the Response API.
     * This built-in tool gives the model the ability to search the web for current information.
     *
     * @return A new ResponseTool configured as a web search tool
     */
    public static ResponseTool webSearchTool() {
        return ResponseTool.builder()
            .type(ResponseToolType.WEB_SEARCH_PREVIEW)
            .build();
    }


    /**
     * Creates a file search tool for the Response API with vector store IDs.
     * This built-in tool allows the model to search through files in the specified vector stores.
     *
     * @param vectorStoreIds List of vector store IDs to search in
     * @return A new ResponseTool configured as a file search tool with vector stores
     */
    public static ResponseTool fileSearchTool(List<String> vectorStoreIds) {
        return ResponseTool.builder()
            .type(ResponseToolType.FILE_SEARCH)
            .vectorStoreIds(vectorStoreIds)
            .build();
    }


    /**
     * Note: Code interpreter (computer_use_preview) is not yet available in the Response API.
     * This method will be implemented when the feature becomes available.
     */
}
