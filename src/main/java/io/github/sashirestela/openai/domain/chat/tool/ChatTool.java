package io.github.sashirestela.openai.domain.chat.tool;

import io.github.sashirestela.openai.function.FunctionDef;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class ChatTool {

    @Required
    private ChatToolType type;

    @Required
    private FunctionDef function;

    public static List<ChatTool> functions(List<FunctionDef> functions) {
        return functions.stream()
                .map(funDef -> new ChatTool(ChatToolType.FUNCTION, funDef))
                .collect(Collectors.toList());
    }

}
