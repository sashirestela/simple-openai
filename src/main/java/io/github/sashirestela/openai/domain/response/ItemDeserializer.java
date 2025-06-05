package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemDeserializer extends JsonDeserializer<Input.Item> {

    private static final Map<String, Class<? extends Input.Item>> mapTypeToClass;
    static {
        mapTypeToClass = new HashMap<>();
        mapTypeToClass.put("file_search_call", Input.Item.FileSearchCallItem.class);
        mapTypeToClass.put("computer_call", Input.Item.ComputerCallItem.class);
        mapTypeToClass.put("computer_call_output", Input.Item.ComputerCallOutputItem.class);
        mapTypeToClass.put("web_search_call", Input.Item.WebSearchCallItem.class);
        mapTypeToClass.put("function_call", Input.Item.FunctionCallItem.class);
        mapTypeToClass.put("function_call_output", Input.Item.FunctionCallOutputItem.class);
        mapTypeToClass.put("reasoning", Input.Item.ReasoningItem.class);
        mapTypeToClass.put("image_generation_call", Input.Item.ImageGenerationCallItem.class);
        mapTypeToClass.put("code_interpreter_call", Input.Item.CodeInterpreterCallItem.class);
        mapTypeToClass.put("local_shell_call", Input.Item.LocalShellCallItem.class);
        mapTypeToClass.put("local_shell_call_output", Input.Item.LocalShellCallOutputItem.class);
        mapTypeToClass.put("mcp_list_tools", Input.Item.McpListToolsItem.class);
        mapTypeToClass.put("mcp_approval_request", Input.Item.McpApprovalRequestItem.class);
        mapTypeToClass.put("mcp_approval_response", Input.Item.McpApprovalResponseItem.class);
        mapTypeToClass.put("mcp_call", Input.Item.McpCallItem.class);
    }

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
