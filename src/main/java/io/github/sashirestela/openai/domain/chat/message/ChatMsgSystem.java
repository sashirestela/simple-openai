package io.github.sashirestela.openai.domain.chat.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.sashirestela.openai.domain.chat.Role;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_EMPTY)
public class ChatMsgSystem extends ChatMsg {

    @JsonInclude private String content;
    private String name;

    public ChatMsgSystem(String content, String name) {
        this.role = Role.SYSTEM;
        this.content = content;
        this.name = name;
    }

    public ChatMsgSystem(String content) {
        this(content, null);
    }
}