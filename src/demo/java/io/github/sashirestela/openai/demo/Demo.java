package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadRun.RequiredAction.SubmitToolOutput.ThreadRunToolCall;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadRunSubmitOutputRequest.ToolOutput;
import io.github.sashirestela.openai.domain.assistant.v2.ToolType;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgTool;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolCall;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolType;
import io.github.sashirestela.openai.function.FunctionCall;
import io.github.sashirestela.openai.function.FunctionDef;
import io.github.sashirestela.openai.function.FunctionExecutor;
import io.github.sashirestela.openai.function.Functional;

import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        List<FunctionDef> functionList = new ArrayList<>();
        functionList.add(FunctionDef.builder()
                .name("getCurrentTemperature")
                .description("Get the current temperature for a specific location")
                .functionalClass(CurrentTemperature.class)
                .build());
        functionList.add(FunctionDef.builder()
                .name("getRainProbability")
                .description("Get the probability of rain for a specific location")
                .functionalClass(RainProbability.class)
                .build());
        var functionExecutor = new FunctionExecutor(functionList);

        List<ChatToolCall> chatToolCalls = List.of(
                new ChatToolCall(0, "1a", ChatToolType.FUNCTION,
                        new FunctionCall("getCurrentTemperature", "{\"location\": \"Lima, Peru\", \"unit\": \"C\"}")),
                new ChatToolCall(1, "2b", ChatToolType.FUNCTION,
                        new FunctionCall("getRainProbability", "{\"location\": \"Lima, Peru\"}")));
        List<ChatMsgTool> chatToolOutputs = functionExecutor.executeAll(chatToolCalls,
                (toolCallId, result) -> new ChatMsgTool(result, toolCallId));
        chatToolOutputs.forEach(System.out::println);

        List<ThreadRunToolCall> runToolCalls = List.of(
                new ThreadRunToolCall("3c", ToolType.FUNCTION,
                        new FunctionCall("getCurrentTemperature", "{\"location\": \"Lima, Peru\", \"unit\": \"C\"}")),
                new ThreadRunToolCall("4d", ToolType.FUNCTION,
                        new FunctionCall("getRainProbability", "{\"location\": \"Lima, Peru\"}")));
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
