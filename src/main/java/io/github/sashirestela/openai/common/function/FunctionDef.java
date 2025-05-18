package io.github.sashirestela.openai.common.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
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

    private Boolean strict;

    @Builder.Default
    private SchemaConverter schemaConverter = JsonSchemaUtil.defaultConverter;

    public static FunctionDef of(Class<? extends Functional> functionalClass) {
        var name = functionalClass.getSimpleName();
        var description = functionalClass.isAnnotationPresent(JsonClassDescription.class)
                ? functionalClass.getAnnotation(JsonClassDescription.class).value()
                : "";
        var strict = Boolean.TRUE;
        return FunctionDef.builder()
                .name(name)
                .description(description)
                .functionalClass(functionalClass)
                .strict(strict)
                .build();
    }

}
