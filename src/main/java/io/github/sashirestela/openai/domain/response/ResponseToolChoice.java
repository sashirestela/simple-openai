package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a tool choice for the Response API. This class can be used to specify how the model
 * should use tools during response generation. The OpenAI Response API supports the following tool
 * choice behaviors: - Auto (default): The model will determine when and how many tools to use. May
 * call zero, one, or multiple functions. - Required: The model must call one or more functions. -
 * None: The model should not use any tools. - Forced Function: The model must call a specific named
 * function. This class provides both static factory methods for common options and a builder for
 * more complex cases. Note that the Response API's function calling format differs from the Chat
 * Completions API: - The Response API uses a different structure for tool definitions - Function
 * calls are returned in the response output array rather than in a choices array - The Response API
 * has more comprehensive tool options Example usage: ```java // Auto - let the model decide
 * .toolChoice(ResponseToolChoice.auto()) // Required - force the model to use at least one tool
 * .toolChoice(ResponseToolChoice.required()) // None - prevent the model from using tools
 * .toolChoice(ResponseToolChoice.none()) // Force a specific function
 * .toolChoice(ResponseToolChoice.function("get_weather")) ```
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseToolChoice {

    private ResponseTool.ResponseToolType type;
    private FunctionChoice function;

    /**
     * Enum for tool choice options that can be used as a string value directly. Used to specify how the
     * model should use tools.
     */
    public enum ToolChoiceType {

        /**
         * Default behavior - The model will determine when and how many tools to use. The model may call
         * zero, one, or multiple functions.
         */
        @JsonProperty("auto")
        AUTO,

        /**
         * The model should use a specific function.
         */
        @JsonProperty("function")
        FUNCTION,

        /**
         * The model should not use any tools.
         */
        @JsonProperty("none")
        NONE,

        /**
         * The model must call one or more functions.
         */
        @JsonProperty("required")
        REQUIRED;

        @Override
        public String toString() {
            if (this == AUTO)
                return "auto";
            if (this == FUNCTION)
                return "function";
            if (this == NONE)
                return "none";
            if (this == REQUIRED)
                return "required";
            return name().toLowerCase();
        }

    }

    /**
     * Represents a function choice for the tool_choice field.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FunctionChoice {

        private String name;

    }

    /**
     * Creates a tool choice that lets the model decide whether to use tools or not. Equivalent to
     * setting "auto" as the tool_choice. This is the default behavior - the model may call zero, one,
     * or multiple functions.
     * 
     * @return A string value "auto" for tool choice
     */
    public static String auto() {
        return ToolChoiceType.AUTO.toString();
    }

    /**
     * Creates a tool choice that forces the model not to use any tools. Equivalent to setting "none" as
     * the tool_choice.
     * 
     * @return A string value "none" for tool choice
     */
    public static String none() {
        return ToolChoiceType.NONE.toString();
    }

    /**
     * Creates a tool choice that requires the model to call one or more functions. Equivalent to
     * setting "required" as the tool_choice.
     * 
     * @return A string value "required" for tool choice
     */
    public static String required() {
        return ToolChoiceType.REQUIRED.toString();
    }

    /**
     * Convenience method to create a tool choice with a string value.
     * 
     * @param toolChoice The tool choice type as a string: "auto", "function", "none", "required"
     * @return The string tool choice
     */
    public static String of(String toolChoice) {
        return toolChoice;
    }

    /**
     * Convenience method to create a tool choice from the enum.
     * 
     * @param type The tool choice type
     * @return The string representation of the tool choice
     */
    public static String of(ToolChoiceType type) {
        return type.toString();
    }

    /**
     * Creates a tool choice that forces the model to use a specific function.
     * 
     * @param functionName The name of the function to use
     * @return A ResponseToolChoice object for function tool choice
     */
    public static ResponseToolChoice function(String functionName) {
        return ResponseToolChoice.builder()
                .type(ResponseTool.ResponseToolType.FUNCTION)
                .function(FunctionChoice.builder().name(functionName).build())
                .build();
    }

    /**
     * Creates a builder for ResponseToolChoice.
     * 
     * @return A new builder instance
     */
    public static ResponseToolChoiceBuilder builder() {
        return new ResponseToolChoiceBuilder();
    }

    /**
     * Builder class for ResponseToolChoice.
     */
    public static class ResponseToolChoiceBuilder {

        private ResponseTool.ResponseToolType type;
        private FunctionChoice function;

        ResponseToolChoiceBuilder() {
        }

        public ResponseToolChoiceBuilder type(ResponseTool.ResponseToolType type) {
            this.type = type;
            return this;
        }

        public ResponseToolChoiceBuilder function(FunctionChoice function) {
            this.function = function;
            return this;
        }

        public ResponseToolChoice build() {
            return new ResponseToolChoice(type, function);
        }

    }

    enum HostedToolType {
        @JsonProperty("file_search")
        FILE_SEARCH,

        @JsonProperty("web_search_preview")
        WEB_SEARCH_PREVIEW,
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class HostedTool {

        public static final HostedTool FILE_SEARCH = new HostedTool(HostedToolType.FILE_SEARCH);
        public static final HostedTool WEB_SEARCH_PREVIEW = new HostedTool(HostedToolType.WEB_SEARCH_PREVIEW);
        private HostedToolType type;

    }

}
