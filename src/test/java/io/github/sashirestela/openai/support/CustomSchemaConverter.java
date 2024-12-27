package io.github.sashirestela.openai.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import io.github.sashirestela.openai.common.function.SchemaConverter;
import io.github.sashirestela.openai.exception.SimpleOpenAIException;

public class CustomSchemaConverter implements SchemaConverter {

    private final SchemaGenerator schemaGenerator;
    private final ObjectMapper objectMapper;
    public static final String JSON_EMPTY_CLASS = "{\"type\":\"object\",\"properties\":{},\"additionalProperties\":false}";

    public CustomSchemaConverter() {
        objectMapper = new ObjectMapper();
        var jacksonModule = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED,
                JacksonOption.RESPECT_JSONPROPERTY_ORDER);
        var configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12,
                OptionPreset.PLAIN_JSON)
                .with(jacksonModule)
                .with(builder -> builder.forTypesInGeneral()
                        .withTypeAttributeOverride(
                                (collectedTypeAttributes, scope, context) -> collectedTypeAttributes
                                        .put("myCustomProperty", true)))
                .without(Option.SCHEMA_VERSION_INDICATOR);
        var config = configBuilder.build();
        schemaGenerator = new SchemaGenerator(config);
    }

    @Override
    public JsonNode convert(Class<?> clazz) {
        JsonNode jsonSchema;
        try {
            jsonSchema = schemaGenerator.generateSchema(clazz);
            if (jsonSchema.get("properties") == null) {
                jsonSchema = objectMapper.readTree(JSON_EMPTY_CLASS);
            }

        } catch (Exception e) {
            throw new SimpleOpenAIException("Cannot generate the Json Schema for the class {0}.", clazz.getName(),
                    e);
        }
        return jsonSchema;
    }

}
