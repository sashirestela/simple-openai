package io.github.sashirestela.openai.support;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;

import io.github.sashirestela.openai.exception.UncheckedException;

public class JsonUtil {
  private static JsonUtil jsonUtil = null;
  private ObjectMapper objectMapper;

  private JsonUtil() {
    objectMapper = new ObjectMapper();
  }

  public static JsonUtil get() {
    if (jsonUtil == null) {
      jsonUtil = new JsonUtil();
    }
    return jsonUtil;
  }

  public <T> String objectToJson(T object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new UncheckedException("Cannot convert the object {0} to Json.", object, e);
    }
  }

  public <T> T jsonToObject(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new UncheckedException("Cannot convert the Json {0} to class {1}.", json, clazz.getName(), e);
    }
  }

  public <T> List<T> jsonToList(String json, Class<T> clazz) {
    try {
      CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
      return objectMapper.readValue(json, listType);
    } catch (JsonProcessingException e) {
      throw new UncheckedException("Cannot convert the Json {0} to List of {1}.", json, clazz.getName(), e);
    }
  }

  public JsonNode classToJsonSchema(Class<?> clazz) {
    JsonNode jsonSchema = null;
    if (clazz.getFields().length > 0) {
      try {
        JacksonModule jacksonModule = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12,
            OptionPreset.PLAIN_JSON)
            .with(jacksonModule)
            .without(Option.SCHEMA_VERSION_INDICATOR);
        SchemaGeneratorConfig config = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(config);
        jsonSchema = generator.generateSchema(clazz);

      } catch (Exception e) {
        throw new UncheckedException("Cannot generate the Json Schema for the class {0}.", clazz.getName(), e);
      }
    } else {
      try {
        jsonSchema = objectMapper.readTree(Constant.JSON_EMPTY_CLASS);
      } catch (JsonProcessingException e) {
        throw new UncheckedException("Cannot generate the Json Schema for the class {0}.", clazz.getName(), e);
      }
    }
    return jsonSchema;
  }
}