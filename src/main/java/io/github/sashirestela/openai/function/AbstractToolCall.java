package io.github.sashirestela.openai.function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public abstract class AbstractToolCall {

    protected String id;
    protected FunctionCall function;

}
