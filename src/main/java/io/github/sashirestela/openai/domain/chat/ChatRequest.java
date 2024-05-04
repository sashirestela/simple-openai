package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.ResponseFormat;
import io.github.sashirestela.openai.tool.Tool;
import io.github.sashirestela.openai.tool.ToolChoice;
import io.github.sashirestela.openai.tool.ToolChoiceOption;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.With;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRequest {

    @Required
    @Singular
    private List<ChatMessage> messages;

    @Required
    private String model;

    @Range(min = -2.0, max = 2.0)
    private Double frequencyPenalty;

    private Map<String, Integer> logitBias;

    private Boolean logprobs;

    @Range(min = 0, max = 20)
    private Integer topLogprobs;

    private Integer maxTokens;

    @Range(min = 1, max = 128)
    private Integer n;

    @Range(min = -2.0, max = 2.0)
    private Double presencePenalty;

    private ResponseFormat responseFormat;

    private Integer seed;

    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 4)
    private Object stop;

    @With
    private Boolean stream;

    @Range(min = 0.0, max = 2.0)
    private Double temperature;

    @Range(min = 0.0, max = 1.0)
    private Double topP;

    @Singular
    private List<Tool> tools;

    @With
    @ObjectType(baseClass = ToolChoiceOption.class)
    @ObjectType(baseClass = ToolChoice.class)
    private Object toolChoice;

    private String user;

}
