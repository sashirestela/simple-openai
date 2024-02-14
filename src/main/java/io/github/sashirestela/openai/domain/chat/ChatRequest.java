package io.github.sashirestela.openai.domain.chat;

import static io.github.sashirestela.cleverclient.util.CommonUtil.isNullOrEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.chat.message.ChatMsg;
import io.github.sashirestela.openai.domain.chat.tool.ChatTool;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoice;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoiceType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.With;

@Getter
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRequest {

    @NonNull private String model;
    @NonNull private List<ChatMsg> messages;
    private ChatRespFmt responseFormat;
    private Integer seed;
    private List<ChatTool> tools;
    private Object toolChoice;
    private Double temperature;
    private Double topP;
    private Integer n;
    @With private Boolean stream;
    private Object stop;
    private Integer maxTokens;
    private Double presencePenalty;
    private Double frequencyPenalty;
    private Map<String, Integer> logitBias;
    private String user;
    private Boolean logprobs;
    private Integer topLogprobs;

    @Builder
    public ChatRequest(@NonNull String model, @NonNull @Singular List<ChatMsg> messages, ChatRespFmt responseFormat,
            Integer seed, @Singular List<ChatTool> tools, Object toolChoice, Double temperature, Double topP, Integer n,
            Boolean stream, Object stop, Integer maxTokens, Double presencePenalty, Double frequencyPenalty,
            Map<String, Integer> logitBias, String user, Boolean logprobs, Integer topLogprobs) {
        if (toolChoice != null &&
                !(toolChoice instanceof ChatToolChoiceType) && !(toolChoice instanceof ChatToolChoice)) {
            throw new SimpleUncheckedException(
                    "The field toolChoice must be ChatToolChoiceType or ChatToolChoice classes.",
                    null, null);
        }
        if (!isNullOrEmpty(tools)) {
            toolChoice = Optional.ofNullable(toolChoice)
                .orElse(ChatToolChoiceType.AUTO);
        }
        if (stop != null && !(stop instanceof String) && !(stop instanceof List
                && ((List<?>) stop).get(0) instanceof String && ((List<?>) stop).size() <= 4)) {
            throw new SimpleUncheckedException(
                    "The field stop must be String or List<String> (max 4 items) classes.",
                    null, null);
        }
        this.model = model;
        this.messages = messages;
        this.responseFormat = responseFormat;
        this.seed = seed;
        this.tools = tools;
        this.toolChoice = toolChoice;
        this.temperature = temperature;
        this.topP = topP;
        this.n = n;
        this.stream = stream;
        this.stop = stop;
        this.maxTokens = maxTokens;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.logitBias = logitBias;
        this.user = user;
        this.logprobs = logprobs;
        this.topLogprobs = topLogprobs;
    }
}