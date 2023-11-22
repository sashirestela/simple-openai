package io.github.sashirestela.openai.domain.chat;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.chat.message.ChatMsg;
import io.github.sashirestela.openai.domain.chat.tool.ChatTool;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoice;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoiceType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;

@Getter
public class ChatRequest {

    private String model;

    private List<ChatMsg> messages;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("response_format")
    private ChatRespFmt responseFormat;

    @JsonInclude(Include.NON_NULL)
    private Integer seed;

    @JsonInclude(Include.NON_NULL)
    private List<ChatTool> tools;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("tool_choice")
    private Object toolChoice;

    @JsonInclude(Include.NON_NULL)
    private Double temperature;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("top_p")
    private Double topP;

    @JsonInclude(Include.NON_NULL)
    private Integer n;

    @With
    @JsonInclude(Include.NON_NULL)
    private Boolean stream;

    @JsonInclude(Include.NON_NULL)
    private Object stop;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("logit_bias")
    private Map<String, Integer> logitBias;

    @JsonInclude(Include.NON_NULL)
    private String user;

    @Builder
    public ChatRequest(@NonNull String model, @NonNull List<ChatMsg> messages, ChatRespFmt responseFormat,
            Integer seed, List<ChatTool> tools, Object toolChoice, Double temperature, Double topP, Integer n,
            Boolean stream, Object stop, Integer maxTokens, Double presencePenalty, Double frequencyPenalty,
            Map<String, Integer> logitBias, String user) {
        if (toolChoice != null &&
                !(toolChoice instanceof ChatToolChoiceType)
                && !(toolChoice instanceof ChatToolChoice)) {
            throw new SimpleUncheckedException(
                    "The field toolChoice must be ChatToolChoiceType or ChatToolChoice classes.",
                    null, null);
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
    }
}