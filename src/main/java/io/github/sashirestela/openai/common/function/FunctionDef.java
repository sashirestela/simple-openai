package io.github.sashirestela.openai.common.function;

import io.github.sashirestela.openai.support.JsonSchemaUtil;
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
    @Builder.Default
    private SchemaConverter schemaConverter = JsonSchemaUtil.defaultConverter;

}
