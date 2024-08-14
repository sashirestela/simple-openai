package io.github.sashirestela.openai.common.function;

import com.fasterxml.jackson.databind.node.ObjectNode;
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

    private boolean strict;

    @NonNull
    private Class<? extends Functional> functionalClass;

    @Builder.Default
    private SchemaConverter schemaConverter = converterWithAdditionalProperties;

    private static final SchemaConverter converterWithAdditionalProperties = c -> {
        var jsonNode = JsonSchemaUtil.defaultConverter.convert(c);
        // Default to no additional properties.
        // Ref: https://platform.openai.com/docs/guides/structured-outputs/additionalproperties-false-must-always-be-set-in-objects
        if (jsonNode.get("additionalProperties") == null) {
            ((ObjectNode) jsonNode).put("additionalProperties", false);
        }
        return jsonNode;
    };

}
