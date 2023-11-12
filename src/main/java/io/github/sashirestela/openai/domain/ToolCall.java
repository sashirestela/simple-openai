package io.github.sashirestela.openai.domain;

import io.github.sashirestela.openai.domain.chat.ChatFunctionCall;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ToolCall {

    private String id;
    private String type;
    private ChatFunctionCall function;

}
