package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.common.tool.ToolCall;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.Chat.Choice;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.AssistantMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ResponseMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ToolMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConversationDemo {

    private SimpleOpenAI openAI;
    private FunctionExecutor functionExecutor;

    private int indexTool;
    private StringBuilder content;
    private StringBuilder functionArgs;

    public ConversationDemo() {
        openAI = SimpleOpenAI.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();
    }

    public void prepareConversation() {
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
        functionExecutor = new FunctionExecutor(functionList);
    }

    public void runConversation() {
        List<ChatMessage> messages = new ArrayList<>();
        var myMessage = System.console().readLine("\nWelcome! Write any message: ");
        messages.add(UserMessage.of(myMessage));
        while (!myMessage.equalsIgnoreCase("exit")) {
            var chatStream = openAI.chatCompletions()
                    .createStream(ChatRequest.builder()
                            .model("gpt-4o-mini")
                            .messages(messages)
                            .tools(functionExecutor.getToolFunctions())
                            .temperature(0.2)
                            .stream(true)
                            .build())
                    .join();

            indexTool = -1;
            content = new StringBuilder();
            functionArgs = new StringBuilder();

            var response = getResponse(chatStream);

            if (response.getMessage().getContent() != null) {
                messages.add(AssistantMessage.of(response.getMessage().getContent()));
            }
            if (response.getFinishReason().equals("tool_calls")) {
                messages.add(response.getMessage());
                var toolCalls = response.getMessage().getToolCalls();
                var toolMessages = functionExecutor.executeAll(toolCalls,
                        (toolCallId, result) -> ToolMessage.of(result, toolCallId));
                messages.addAll(toolMessages);
            } else {
                myMessage = System.console().readLine("\n\nWrite any message (or write 'exit' to finish): ");
                messages.add(UserMessage.of(myMessage));
            }
        }
    }

    private Choice getResponse(Stream<Chat> chatStream) {
        var choice = new Choice();
        choice.setIndex(0);
        var chatMsgResponse = new ResponseMessage();
        List<ToolCall> toolCalls = new ArrayList<>();

        chatStream.forEach(responseChunk -> {
            var choices = responseChunk.getChoices();
            if (!choices.isEmpty()) {
                var innerChoice = choices.get(0);
                var delta = innerChoice.getMessage();
                if (delta.getRole() != null) {
                    chatMsgResponse.setRole(delta.getRole());
                }
                if (delta.getContent() != null && !delta.getContent().isEmpty()) {
                    content.append(delta.getContent());
                    System.out.print(delta.getContent());
                }
                if (delta.getToolCalls() != null) {
                    var toolCall = delta.getToolCalls().get(0);
                    if (toolCall.getIndex() != indexTool) {
                        if (!toolCalls.isEmpty()) {
                            toolCalls.get(toolCalls.size() - 1).getFunction().setArguments(functionArgs.toString());
                            functionArgs = new StringBuilder();
                        }
                        toolCalls.add(toolCall);
                        indexTool++;
                    } else {
                        functionArgs.append(toolCall.getFunction().getArguments());
                    }
                }
                if (innerChoice.getFinishReason() != null) {
                    if (content.length() > 0) {
                        chatMsgResponse.setContent(content.toString());
                    }
                    if (!toolCalls.isEmpty()) {
                        toolCalls.get(toolCalls.size() - 1).getFunction().setArguments(functionArgs.toString());
                        chatMsgResponse.setToolCalls(toolCalls);
                    }
                    choice.setMessage(chatMsgResponse);
                    choice.setFinishReason(innerChoice.getFinishReason());
                }
            }
        });
        return choice;
    }

    public static void main(String[] args) {
        var demo = new ConversationDemo();
        demo.prepareConversation();
        demo.runConversation();
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
            double centigrades = Math.random() * (40.0 - 10.0) + 10.0;
            double fahrenheit = centigrades * 9.0 / 5.0 + 32.0;
            String shortUnit = unit.substring(0, 1).toUpperCase();
            return shortUnit.equals("C") ? centigrades : (shortUnit.equals("F") ? fahrenheit : 0.0);
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
