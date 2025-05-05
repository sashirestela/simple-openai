package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.With;

import java.util.List;
import java.util.Map;

/**
 * Request class for the OpenAI Response API.
 * 
 * The Response API is OpenAI's latest API for creating text-only outputs from models
 * with optional tools. It offers these advantages over the older Chat Completions API:
 * - Output array instead of choices array
 * - Different structure for function calls
 * - More comprehensive reasoning support
 * - Built-in conversation state management through previous_response_id
 * - Structured JSON outputs via the text.format parameter
 * 
 * Example usage:
 * ```java
 * // Simple text completion
 * ResponseRequest request = ResponseRequest.builder()
 *     .input("What is the capital of France?")
 *     .model("gpt-4o")
 *     .build();
 * 
 * // Using the web search tool (available in the Response API)
 * ResponseRequest webSearchRequest = ResponseRequest.builder()
 *     .input("What was a positive news story from today?")
 *     .model("gpt-4o")
 *     .tool(ResponseTool.webSearchTool())
 *     .build();
 * 
 * // Using a custom function tool
 * ResponseRequest functionRequest = ResponseRequest.builder()
 *     .input("What is the weather in New York?")
 *     .model("gpt-4o")
 *     .tool(ResponseTool.functionTool(
 *         "get_weather",
 *         "Get current weather in a location",
 *         weatherParameters
 *     ))
 *     .build();
 * ```
 * 
 * Note: Not all tools from the Chat Completions API are available in the Response API.
 * The currently supported tools are:
 * - Function tools (custom developer-defined functions)
 * - Web search (web_search_preview)
 * - File search (file_search)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseRequest {

    @Required
    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = Input.class, firstGroup = true)
    private Object input;

    @Required
    private String model;

    private List<ResponseInclude> include;

    private String instructions;

    private Integer maxOutputTokens;

    @Size(max = 16)
    private Map<String, String> metadata;

    private Boolean parallelToolCalls;

    private String previousResponseId;

    private Reasoning reasoning;

    private Boolean store;

    @With
    private Boolean stream;

    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    private ResponseText text;

    @ObjectType(baseClass = ToolChoiceOption.class)
    @ObjectType(baseClass = ResponseToolChoice.class)
    @ObjectType(baseClass = String.class)
    private Object toolChoice;

    @Singular
    private List<ResponseTool> tools;

    @Range(min = 0.0, max = 1.0)
    private Double topP;

    private Truncation truncation;

    private String user;

    public enum ResponseInclude {

        @JsonProperty("file_search_call.results")
        FILE_SEARCH_CALL_RESULTS,

        @JsonProperty("message.input_image.image_url")
        MESSAGE_INPUT_IMAGE_URL,

        @JsonProperty("computer_call_output.output.image_url")
        COMPUTER_CALL_OUTPUT_IMAGE_URL;

    }

    public enum Truncation {

        @JsonProperty("auto")
        AUTO,

        @JsonProperty("disabled")
        DISABLED;

    }

}
