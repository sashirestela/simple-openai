package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.sashirestela.openai.tool.Tool;
import io.github.sashirestela.openai.tool.ToolType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssistantTool extends Tool {

    public static final AssistantTool CODE_INTERPRETER = (AssistantTool) new Tool(ToolType.CODE_INTERPRETER, null);
    public static final AssistantTool FILE_SEARCH = (AssistantTool) new Tool(ToolType.FILE_SEARCH, null);

}
