package io.github.sashirestela.openai.domain.chat.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.domain.chat.Role;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ChatMsgTool extends ChatMsg {

    @JsonInclude()
    private String content;

    @NonNull
    @JsonProperty("tool_call_id")
    private String toolCallId;

    public ChatMsgTool(String content, String toolCallId) {
        this.role = Role.TOOL;
        this.content = content;
        this.toolCallId = toolCallId;
    }
}