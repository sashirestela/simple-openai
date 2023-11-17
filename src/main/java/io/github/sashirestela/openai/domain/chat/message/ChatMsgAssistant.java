package io.github.sashirestela.openai.domain.chat.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.domain.chat.Role;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolCall;
import lombok.Getter;

@Getter
public class ChatMsgAssistant extends ChatMsg {

    @JsonInclude()
    private String content;

    @JsonInclude(Include.NON_NULL)
    private String name;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("tool_calls")
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