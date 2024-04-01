package io.github.sashirestela.openai.domain.chat.tool;

import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatFunctionName {

    @Required
    private String name;

}
