package io.github.sashirestela.openai.domain.chat.tool;

import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatTool {

    @Required
    private ChatToolType type;

    @Required
    private ChatFunction function;

}
