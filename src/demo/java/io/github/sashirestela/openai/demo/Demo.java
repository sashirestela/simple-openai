package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest.ToolOutput;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ToolMessage;
import io.github.sashirestela.openai.function.FunctionCall;
import io.github.sashirestela.openai.function.FunctionDef;
import io.github.sashirestela.openai.function.FunctionExecutor;
import io.github.sashirestela.openai.function.Functional;
import io.github.sashirestela.openai.tool.ToolCall;
import io.github.sashirestela.openai.tool.ToolType;

import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        List<FunctionDef> functionList = new ArrayList<>();
        functionList.add(FunctionDef.builder()
                .name("CurrentTemperature")
                .description("Get the current temperature for a specific location")
                .functionalClass(CurrentTemperature.class)
                .build());
        functionList.add(FunctionDef.builder()
                .name("RainProbability")
                .description("Get the probability of rain for a specific location")
                .functionalClass(RainProbability.class)
                .build());
        var functionExecutor = new FunctionExecutor(functionList);

        List<ToolCall> chatToolCalls = List.of(
                new ToolCall(0, "1a", ToolType.FUNCTION,
                        new FunctionCall("CurrentTemperature", "{\"location\": \"Lima, Peru\", \"unit\": \"C\"}")),
                new ToolCall(1, "2b", ToolType.FUNCTION,
                        new FunctionCall("RainProbability", "{\"location\": \"Lima, Peru\"}")));
        List<ToolMessage> chatToolOutputs = functionExecutor.executeAll(chatToolCalls,
                (toolCallId, result) -> ToolMessage.of(result, toolCallId));
        chatToolOutputs.forEach(System.out::println);

        List<ToolCall> runToolCalls = List.of(
                new ToolCall(null, "3c", ToolType.FUNCTION,
                        new FunctionCall("CurrentTemperature", "{\"location\": \"Lima, Peru\", \"unit\": \"C\"}")),
                new ToolCall(null, "4d", ToolType.FUNCTION,
                        new FunctionCall("RainProbability", "{\"location\": \"Lima, Peru\"}")));
        List<ToolOutput> runToolOutputs = functionExecutor.executeAll(runToolCalls,
                (toolCallId, result) -> ToolOutput.builder().toolCallId(toolCallId).output(result).build());
        runToolOutputs.forEach(System.out::println);
    }

    public static class CurrentTemperature implements Functional {

        @JsonPropertyDescription("The city and state, e.g., San Francisco, CA")
        @JsonProperty(required = true)
        public String location;

        @JsonPropertyDescription("The temperature unit to use. Infer this from the user's location.")
        @JsonProperty(required = true)
        public String unit;

        @Override
        public Object execute() {
            return Math.random() * 45;
        }

    }

    public static class RainProbability implements Functional {

        @JsonPropertyDescription("The city and state, e.g., San Francisco, CA")
        @JsonProperty(required = true)
        public String location;

        @Override
        public Object execute() {
            return Math.random() * 100;
        }

    }

}
