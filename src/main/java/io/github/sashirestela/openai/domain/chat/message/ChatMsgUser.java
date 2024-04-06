package io.github.sashirestela.openai.domain.chat.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.sashirestela.openai.domain.chat.Role;
import io.github.sashirestela.openai.domain.chat.content.ContentPart;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_EMPTY)
public class ChatMsgUser extends ChatMsg {

    @Required
    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = ContentPart.class, firstGroup = true)
    private Object content;

    private String name;

    public ChatMsgUser(Object content, String name) {
        this.role = Role.USER;
        this.content = content;
        this.name = name;
    }

    public ChatMsgUser(Object content) {
        this(content, null);
    }

}
