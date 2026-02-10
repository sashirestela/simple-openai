package io.github.sashirestela.openai.demo;

import io.github.sashirestela.cleverclient.Event;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.conversation.ConversationRequest;
import io.github.sashirestela.openai.domain.response.Input.Item.OutputMessageItem;
import io.github.sashirestela.openai.domain.response.ResponseRequest;
import io.github.sashirestela.openai.domain.response.stream.EventName;
import io.github.sashirestela.openai.domain.response.stream.ResponseOutputItemEvent;
import io.github.sashirestela.openai.domain.response.stream.ResponseOutputTextEvent;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Demo showing multi-turn conversation using the new Conversations API. This uses durable,
 * server-managed conversation state instead of manually chaining response IDs with
 * previousResponseId.
 */
public class ConversationV4Demo {

    private static final String EXIT = "x";
    private static final String HINT = "\nAsk anything ('" + EXIT + "' to finish): ";

    private SimpleOpenAI openAI;
    private String conversationId;

    public ConversationV4Demo() {
        this.openAI = SimpleOpenAI.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();
    }

    public void prepareConversation() {
        // Create a new conversation with metadata
        var conversationRequest = ConversationRequest.builder()
                .metadata(Map.of("demo", "ConversationV4Demo", "created_by", "simple-openai"))
                .build();
        var conversation = this.openAI.conversations().create(conversationRequest).join();
        this.conversationId = conversation.getId();
        System.out.println("Created conversation: " + this.conversationId);
    }

    public void runConversation() {
        String myQuestion = "";
        while (true) {
            myQuestion = System.console().readLine(HINT);
            if (myQuestion.equalsIgnoreCase(EXIT)) {
                break;
            }
            // Use the conversation parameter instead of previousResponseId
            var responseRequest = ResponseRequest.builder()
                    .instructions("You are a helpful assistant. Respond in a concise way.")
                    .input(myQuestion)
                    .conversation(this.conversationId)
                    .model("gpt-4o-mini")
                    .temperature(0.1)
                    .build();
            var responseStream = this.openAI.responses().createStream(responseRequest).join();
            handleResponseEvents(responseStream);
        }
    }

    private void handleResponseEvents(Stream<Event> responseStream) {
        responseStream.forEach(event -> {
            switch (event.getName()) {
                case EventName.RESPONSE_OUTPUT_TEXT_DELTA:
                    var respOutTextDelta = (ResponseOutputTextEvent) event.getData();
                    var delta = respOutTextDelta.getText();
                    System.out.print(delta);
                    break;
                case EventName.RESPONSE_OUTPUT_TEXT_DONE:
                    System.out.println();
                    break;
                case EventName.RESPONSE_OUTPUT_ITEM_ADDED:
                    var respOutItemAdded = (ResponseOutputItemEvent) event.getData();
                    if (respOutItemAdded.getItem() instanceof OutputMessageItem) {
                        System.out.println("====> Message ...");
                    }
                    break;
                default:
                    break;
            }
        });
    }

    public void cleanConversation() {
        var items = this.openAI.conversationItems().getList(this.conversationId).join();
        items.getData().forEach(item -> {
            this.openAI.conversationItems().delete(this.conversationId, item.getId()).join();
        });

        this.openAI.conversations().delete(this.conversationId).join();
        System.out.println("Deleted conversation: " + this.conversationId);
        this.openAI.shutDown();
    }

    public static void main(String[] args) {
        var demo = new ConversationV4Demo();
        demo.prepareConversation();
        demo.runConversation();
        demo.cleanConversation();
    }

}
