package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class FilterDeserializer extends JsonDeserializer<ResponseTool.Filter> {

    @Override
    public ResponseTool.Filter deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        var mapper = (ObjectMapper) jp.getCodec();
        var node = (JsonNode) mapper.readTree(jp);
        var type = node.has("type") ? node.get("type").asText() : null;
        if (existsEnumValue(ResponseTool.ComparisonOperator.class, type)) {
            return mapper.treeToValue(node, ResponseTool.Filter.ComparisonFilter.class);
        } else if (existsEnumValue(ResponseTool.LogicalOperator.class, type)) {
            return mapper.treeToValue(node, ResponseTool.Filter.CompoundFilter.class);
        } else {
            throw new IOException("The value '" + type + "' is not mapped to any Enum.");
        }
    }

    private <E extends Enum<E>> boolean existsEnumValue(Class<E> enumClass, String value) {
        if (value == null) {
            return false;
        }
        for (E enumValue : enumClass.getEnumConstants()) {
            var enumName = enumValue.name();
            if (enumName.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

}
