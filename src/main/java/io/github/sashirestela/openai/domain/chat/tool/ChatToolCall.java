package io.github.sashirestela.openai.domain.chat.tool;

import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatToolCall {

    @Required
    private String id;

    @Required
    private ChatToolType type;

    @Required
    private ChatFunctionCall function;

}
