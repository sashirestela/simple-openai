package io.github.sashirestela.openai.domain.chat.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.chat.Role;
import lombok.Getter;
import lombok.NonNull;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMsgTool extends ChatMsg {

    @JsonInclude private String content;
    @NonNull private String toolCallId;

    public ChatMsgTool(String content, @NonNull String toolCallId) {
        this.role = Role.TOOL;
        this.content = content;
        this.toolCallId = toolCallId;
    }
}