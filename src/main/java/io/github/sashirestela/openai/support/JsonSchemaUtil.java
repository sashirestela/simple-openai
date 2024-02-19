package io.github.sashirestela.openai.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;

import io.github.sashirestela.openai.SimpleUncheckedException;

public class JsonSchemaUtil {

    public static final String JSON_EMPTY_CLASS = "{\"type\":\"object\",\"properties\":{}}";
    private static ObjectMapper objectMapper = new ObjectMapper();

    private JsonSchemaUtil() {
    }

    public static JsonNode classToJsonSchema(Class<?> clazz) {
        JsonNode jsonSchema = null;
        try {
            var jacksonModule = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
            var configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12,
                    OptionPreset.PLAIN_JSON)
                    .with(jacksonModule)
                    .without(Option.SCHEMA_VERSION_INDICATOR);
            var config = configBuilder.build();
            var generator = new SchemaGenerator(config);
            jsonSchema = generator.generateSchema(clazz);
            if (jsonSchema.get("properties") == null) {
                jsonSchema = objectMapper.readTree(JSON_EMPTY_CLASS);
            }

        } catch (Exception e) {
            throw new SimpleUncheckedException("Cannot generate the Json Schema for the class {0}.",
                    clazz.getName(), e);
        }
        return jsonSchema;
    }
}