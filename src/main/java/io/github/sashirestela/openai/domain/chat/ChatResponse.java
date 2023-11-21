package io.github.sashirestela.openai.domain.chat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.domain.OpenAIUsage;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ChatResponse {

    private String id;

    private String object;

    private Long created;

    private String model;

    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    private List<Choice> choices;

    private OpenAIUsage usage;

    public ChatMsgResponse firstMessage() {
        return getChoices().get(0).getMessage();
    }

    public String firstContent() {
        return firstMessage().getContent();
    }
}