package io.github.sashirestela.openai.domain.chat.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.github.sashirestela.openai.support.JsonUtil;

public class ParametersSerializer extends JsonSerializer<Class<?>> {

  @Override
  public void serialize(Class<?> params,
      JsonGenerator jsonGen,
      SerializerProvider serializer) throws IOException {
    JsonNode jsonSchema = JsonUtil.get().classToJsonSchema(params);
    jsonGen.writeObject(jsonSchema);
  }
}