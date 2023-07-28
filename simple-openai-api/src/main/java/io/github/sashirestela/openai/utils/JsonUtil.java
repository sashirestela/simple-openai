package io.github.sashirestela.openai.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;

import java.util.List;

public class JsonUtil {
  private static JsonUtil jsonUtil = null;
  private ObjectMapper objectMapper;

  private JsonUtil() {
    objectMapper = new ObjectMapper();
  }

  public static JsonUtil one() {
    if (jsonUtil == null) {
      jsonUtil = new JsonUtil();
    }
    return jsonUtil;
  }

  public <T> String objectToJson(T object) {
    try {
		  return objectMapper.writeValueAsString(object);
  	} catch (JsonProcessingException e) {
  		throw new RuntimeException(e);
  	}
  }

  public <T> T jsonToObject(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
  		throw new RuntimeException(e);
  	}
  }

  public <T> List<T> jsonToList(String json, Class<T> clazz) {
    try {
      TypeReference<List<T>> typeReference = new TypeReference<List<T>>() {};
      return objectMapper.readValue(json, typeReference);
    } catch (JsonProcessingException e) {
  		throw new RuntimeException(e);
  	}
  }

  public JsonNode classToJsonSchema(Class<?> clazz) {
    JsonNode jsonSchema;
    if (clazz.getFields().length > 0) {
      JacksonModule jacksonModule = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
      SchemaGeneratorConfigBuilder configBuilder =
        new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
        .with(jacksonModule)
        .without(Option.SCHEMA_VERSION_INDICATOR);
      SchemaGeneratorConfig config = configBuilder.build();
      SchemaGenerator generator = new SchemaGenerator(config);
      jsonSchema = generator.generateSchema(clazz);
    } else {
      try {
        jsonSchema = objectMapper.readTree(Constant.JSON_EMPTY_CLASS);
      } catch (JsonProcessingException e) {
    		throw new RuntimeException(e);
    	}
    }
    return jsonSchema;
  }
}