package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class ItemDeserializer extends JsonDeserializer<Input.Item> {

    public static final Map<String, Class<? extends Input.Item>> mapTypeToClass = Map.of(
            "file_search_call", Input.Item.FileSearchCallItem.class,
            "computer_call", Input.Item.ComputerCallItem.class,
            "computer_call_output", Input.Item.ComputerCallOutputItem.class,
            "web_search_call", Input.Item.WebSearchCallItem.class,
            "function_call", Input.Item.FunctionCallItem.class,
            "function_call_output", Input.Item.FunctionCallOutputItem.class,
            "reasoning", Input.Item.ReasoningItem.class);

    @Override
    public Input.Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        var mapper = (ObjectMapper) jp.getCodec();
        var node = (JsonNode) mapper.readTree(jp);
        var type = node.has("type") ? node.get("type").asText() : null;
        var role = node.has("role") ? node.get("role").asText() : null;
        if ("message".equals(type)) {
            if ("assistant".equals(role)) {
                return mapper.treeToValue(node, Input.Item.OutputMessageItem.class);
            } else {
                return mapper.treeToValue(node, Input.Item.InputMessageItem.class);
            }
        } else {
            return mapper.treeToValue(node, mapTypeToClass.get(type));
        }
    }

}
