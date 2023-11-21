package io.github.sashirestela.openai.domain.chat.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.domain.chat.tool.ChatToolCall;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChatMsgResponse extends ChatMsg {
    
    private String content;

    @JsonProperty("tool_calls")
    private List<ChatToolCall> toolCalls;
}