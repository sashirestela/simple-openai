package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.chat.message.ChatMsg;
import io.github.sashirestela.openai.domain.chat.tool.ChatTool;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoice;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoiceType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
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
    private String model;

    @Required
    @Singular
    private List<ChatMsg> messages;

    private ChatRespFmt responseFormat;

    private Integer seed;

    @Singular
    private List<ChatTool> tools;

    @With
    @ObjectType(baseClass = ChatToolChoiceType.class)
    @ObjectType(baseClass = ChatToolChoice.class)
    private Object toolChoice;

    private Double temperature;

    private Double topP;

    private Integer n;

    @With
    private Boolean stream;

    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 4)
    private Object stop;

    private Integer maxTokens;

    private Double presencePenalty;

    private Double frequencyPenalty;

    private Map<String, Integer> logitBias;

    private String user;

    private Boolean logprobs;

    private Integer topLogprobs;

}
