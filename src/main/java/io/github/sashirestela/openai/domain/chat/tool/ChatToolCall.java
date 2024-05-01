package io.github.sashirestela.openai.domain.chat.tool;

import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatToolCall {

    private Integer index;

    @Required
    private String id;

    @Required
    private ChatToolType type;

    @Required
    private ChatFunctionCall function;

}
