package io.github.sashirestela.openai.domain.chat.message;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.domain.chat.Role;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMsgTool extends ChatMsg {

    @Required
    private String content;

    @Required
    private String toolCallId;

    public ChatMsgTool(String content, String toolCallId) {
        this.role = Role.TOOL;
        this.content = content;
        this.toolCallId = toolCallId;
    }

}
