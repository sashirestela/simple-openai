package io.github.sashirestela.openai.function;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class FunctionDef {

    @NonNull
    private String name;

    private String description;

    @NonNull
    private Class<? extends Functional> functionalClass;

}
