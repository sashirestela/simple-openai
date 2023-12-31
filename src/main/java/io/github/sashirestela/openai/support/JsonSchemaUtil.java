package io.github.sashirestela.openai.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;

import io.github.sashirestela.cleverclient.util.Constant;
import io.github.sashirestela.openai.SimpleUncheckedException;

public class JsonSchemaUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private JsonSchemaUtil() {
    }

    public static JsonNode classToJsonSchema(Class<?> clazz) {
        JsonNode jsonSchema = null;
        if (clazz.getFields().length > 0) {
            try {
                var jacksonModule = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
                var configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12,
                        OptionPreset.PLAIN_JSON)
                        .with(jacksonModule)
                        .without(Option.SCHEMA_VERSION_INDICATOR);
                var config = configBuilder.build();
                var generator = new SchemaGenerator(config);
                jsonSchema = generator.generateSchema(clazz);

            } catch (Exception e) {
                throw new SimpleUncheckedException("Cannot generate the Json Schema for the class {0}.",
                        clazz.getName(), e);
            }
        } else {
            try {
                jsonSchema = objectMapper.readTree(Constant.JSON_EMPTY_CLASS);
            } catch (JsonProcessingException e) {
                throw new SimpleUncheckedException("Cannot generate the Json Schema for the class {0}.",
                        clazz.getName(), e);
            }
        }
        return jsonSchema;
    }
}