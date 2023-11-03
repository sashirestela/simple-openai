package io.github.sashirestela.openai.function;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import io.github.sashirestela.openai.support.JsonSchemaUtil;

public class ParametersSerializer extends JsonSerializer<Class<?>> {

    @Override
    public void serialize(Class<?> params,
            JsonGenerator jsonGen,
            SerializerProvider serializer) throws IOException {
        var jsonSchema = JsonSchemaUtil.classToJsonSchema(params);
        jsonGen.writeObject(jsonSchema);
    }
}