package io.github.sashirestela.openai.domain.chat.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class ChatTool {

    @NonNull
    private ChatToolType type;
    @NonNull
    private ChatFunction function;

}
