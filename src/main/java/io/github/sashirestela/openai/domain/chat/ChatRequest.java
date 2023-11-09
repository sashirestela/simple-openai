package io.github.sashirestela.openai.domain.chat;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
@Builder
public class ChatRequest {

    @NonNull
    private String model;

    @NonNull
    private List<ChatMessage> messages;

    @JsonInclude(Include.NON_NULL)
    private List<ChatFunction> functions;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("function_call")
    private String functionCall;

    @JsonInclude(Include.NON_NULL)
    private Double temperature;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("top_p")
    private Double topP;

    @JsonInclude(Include.NON_NULL)
    private Integer n;

    @JsonInclude(Include.NON_NULL)
    private Boolean stream;

    @JsonInclude(Include.NON_NULL)
    private List<String> stop;

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

}