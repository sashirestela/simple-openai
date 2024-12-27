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
import io.github.sashirestela.openai.common.function.SchemaConverter;
import io.github.sashirestela.openai.exception.SimpleOpenAIException;

import static io.github.sashirestela.openai.support.JsonSchemaUtil.JSON_EMPTY_CLASS;

public class DefaultSchemaConverter implements SchemaConverter {

    private SchemaGenerator schemaGenerator;
    private ObjectMapper objectMapper;

    public DefaultSchemaConverter() {
        this(Boolean.FALSE);
    }

    public DefaultSchemaConverter(Boolean isStructuredOutput) {
        objectMapper = new ObjectMapper();
        JacksonModule jacksonModule = null;
        if (Boolean.TRUE.equals(isStructuredOutput)) {
            jacksonModule = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_ORDER);
        } else {
            jacksonModule = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_ORDER,
                    JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
        }
        var configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
                .with(jacksonModule)
                .with(Option.FORBIDDEN_ADDITIONAL_PROPERTIES_BY_DEFAULT)
                .without(Option.STANDARD_FORMATS)
                .without(Option.SCHEMA_VERSION_INDICATOR);
        if (Boolean.TRUE.equals(isStructuredOutput)) {
            configBuilder.forFields().withRequiredCheck(field -> Boolean.TRUE);
        }
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
            throw new SimpleOpenAIException("Cannot generate the JsonSchema for the class {0}.", clazz.getName(), e);
        }
        return jsonSchema;
    }

}
