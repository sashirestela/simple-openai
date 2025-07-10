package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import io.github.sashirestela.openai.domain.chat.ChatRequest.ServiceTier;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.Schema;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.RequiredIfNull;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.With;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@RequiredIfNull(fields = { "input", "model" }, dependsOn = "prompt")
public class ResponseRequest {

    @ObjectType(baseClass = String.class)
    @ObjectType(schema = Schema.COLL, baseClass = Input.class)
    private Object input;

    private String model;

    private Boolean background;

    private List<ResponseInclude> include;

    private String instructions;

    private Long maxOutputTokens;

    @Size(max = 16)
    private Map<String, String> metadata;

    private Boolean parallelToolCalls;

    private String previousResponseId;

    private Prompt prompt;

    private Reasoning reasoning;

    private ServiceTier serviceTier;

    private Boolean store;

    @With
    private Boolean stream;

    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    private ResponseText text;

    @ObjectType(baseClass = { ToolChoiceOption.class, ResponseToolChoice.HostedTool.class,
            ResponseToolChoice.FunctionTool.class, ResponseToolChoice.MCPTool.class })
    private Object toolChoice;

    @Singular
    private List<ResponseTool> tools;

    @Range(min = 0, max = 20)
    private Integer topLogprobs;

    @Range(min = 0.0, max = 1.0)
    private Double topP;

    private Truncation truncation;

    private String user;

    public enum ResponseInclude {

        @JsonProperty("code_interpreter_call.outputs")
        CODE_INTERPRETER_CALL_OUTPUTS,

        @JsonProperty("computer_call_output.output.image_url")
        COMPUTER_CALL_OUTPUT_IMAGE_URL,

        @JsonProperty("file_search_call.results")
        FILE_SEARCH_CALL_RESULTS,

        @JsonProperty("message.input_image.image_url")
        MESSAGE_INPUT_IMAGE_URL,

        @JsonProperty("message.output_text.logprobs")
        MESSAGE_OUTPUT_TEXT_LOGPROBS,

        @JsonProperty("reasoning.encrypted_content")
        REASONING_ENCRYPTED_CONTENT;

    }

    public enum Truncation {

        @JsonProperty("auto")
        AUTO,

        @JsonProperty("disabled")
        DISABLED;

    }

}
