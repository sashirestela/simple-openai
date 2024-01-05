package io.github.sashirestela.openai.domain.chat.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatFunctionCall {

    private String name;
    private String arguments;

}