package io.github.sashirestela.openai.domain.chat.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.chat.Role;
import io.github.sashirestela.openai.domain.chat.content.ContentPart;
import lombok.Getter;
import lombok.NonNull;

@Getter
@JsonInclude(Include.NON_EMPTY)
public class ChatMsgUser extends ChatMsg {

    private Object content;
    private String name;

    public ChatMsgUser(@NonNull Object content, String name) {
        if (!(content instanceof String) &&
                !(content instanceof List && ((List<?>) content).get(0) instanceof ContentPart)) {
            throw new SimpleUncheckedException("The field content must be String or List<ContentPart> classes.",
                    null, null);
        }
        this.role = Role.USER;
        this.content = content;
        this.name = name;
    }

    public ChatMsgUser(@NonNull Object content) {
        this(content, null);
    }
}