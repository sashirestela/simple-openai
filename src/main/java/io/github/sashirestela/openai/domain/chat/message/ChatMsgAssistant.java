package io.github.sashirestela.openai.domain.chat.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.chat.Role;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolCall;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMsgAssistant extends ChatMsg {

    @JsonInclude private String content;
    private String name;
    private List<ChatToolCall> toolCalls;

    public ChatMsgAssistant(String content, String name, List<ChatToolCall> toolCalls) {
        this.role = Role.ASSISTANT;
        this.content = content;
        this.name = name;
        this.toolCalls = toolCalls;
    }

    public ChatMsgAssistant(String content, List<ChatToolCall> toolCalls) {
        this(content, null, toolCalls);
    }

    public ChatMsgAssistant(String content, String name) {
        this(content, name, null);
    }

    public ChatMsgAssistant(String content) {
        this(content, null, null);
    }
}