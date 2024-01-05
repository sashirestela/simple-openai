package io.github.sashirestela.openai.domain.chat.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatToolCall {
    
    @NonNull private String id;
    @NonNull private ChatToolType type;
    @NonNull private ChatFunctionCall function;

}