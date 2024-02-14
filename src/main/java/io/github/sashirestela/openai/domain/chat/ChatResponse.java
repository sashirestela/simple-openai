package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.OpenAIUsage;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgResponse;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatResponse {

    private String id;
    private String object;
    private Long created;
    private String model;
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