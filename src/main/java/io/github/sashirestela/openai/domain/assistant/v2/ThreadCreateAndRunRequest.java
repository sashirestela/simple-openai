package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.With;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadCreateAndRunRequest {

    @Required
    private String assistantId;

    private ThreadRequest thread;

    private String model;

    private String instructions;
    @Singular
    @Size(max = 20)
    private List<AssistantTool> tools;

    private ToolResource toolResources;

    @Size(max = 16)
    private Map<String, String> metadata;

    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    @Range(min = 0.0, max = 1.0)
    private Double topP;

    @With
    private Boolean stream;

    @Range(min = 256)
    private Integer maxPromptTokens;

    @Range(min = 256)
    private Integer maxCompletionTokens;

    private ThreadRunRequest.TruncationStrategy truncationStrategy;

    @ObjectType(baseClass = ToolChoice.ToolChoiceOption.class)
    @ObjectType(baseClass = ToolChoice.class)
    private Object toolChoice;

    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = AssistantResponseFormat.class)
    private Object responseFormat;

}
