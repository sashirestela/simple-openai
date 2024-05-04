package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.Usage;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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
    private Usage usage;

    public ChatMsgResponse firstMessage() {
        return getChoices().get(0).getMessage();
    }

    public String firstContent() {
        return firstMessage().getContent();
    }

}
