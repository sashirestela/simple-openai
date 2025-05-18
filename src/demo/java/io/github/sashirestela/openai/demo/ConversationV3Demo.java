package io.github.sashirestela.openai.demo;

import io.github.sashirestela.cleverclient.Event;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.demo.util.UtilFunctions;
import io.github.sashirestela.openai.domain.assistant.RankingOption;
import io.github.sashirestela.openai.domain.assistant.RankingOption.RankerType;
import io.github.sashirestela.openai.domain.assistant.VectorStoreRequest;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.domain.response.Input.Item.FileSearchCallItem;
import io.github.sashirestela.openai.domain.response.Input.Item.FunctionCallItem;
import io.github.sashirestela.openai.domain.response.Input.Item.FunctionCallOutputItem;
import io.github.sashirestela.openai.domain.response.Input.Item.OutputMessageItem;
import io.github.sashirestela.openai.domain.response.Input.Item.WebSearchCallItem;
import io.github.sashirestela.openai.domain.response.ResponseRequest;
import io.github.sashirestela.openai.domain.response.ResponseTool;
import io.github.sashirestela.openai.domain.response.ResponseTool.ContextSize;
import io.github.sashirestela.openai.domain.response.ResponseTool.FileSearchResponseTool;
import io.github.sashirestela.openai.domain.response.ResponseTool.FunctionResponseTool;
import io.github.sashirestela.openai.domain.response.ResponseTool.Location;
import io.github.sashirestela.openai.domain.response.ResponseTool.WebSearchResponseTool;
import io.github.sashirestela.openai.domain.response.stream.EventName;
import io.github.sashirestela.openai.domain.response.stream.ResponseEvent;
import io.github.sashirestela.openai.domain.response.stream.ResponseFunctionCallArgsEvent;
import io.github.sashirestela.openai.domain.response.stream.ResponseOutputItemEvent;
import io.github.sashirestela.openai.domain.response.stream.ResponseOutputTextEvent;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ConversationV3Demo {

    private static final String EXIT = "x";
    private static final String HINT = "\nAsk anything ('" + EXIT + "' to finish): ";

    private SimpleOpenAI openAI;
    private FunctionExecutor functionExecutor;
    private String fileId;
    private String vectorStoreId;
    private String responseId;
    private List<String> responseIdList;
    private List<ResponseTool> responseToolList;

    public ConversationV3Demo() {
        this.openAI = SimpleOpenAI.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();
    }

    public void prepareConversation() {
        var funcDefList = Arrays.asList(
                FunctionDef.of(UtilFunctions.CurrentTemperature.class),
                FunctionDef.of(UtilFunctions.RainProbability.class));
        this.functionExecutor = new FunctionExecutor(funcDefList);
        var file = this.openAI.files()
                .create(FileRequest.builder()
                        .file(Paths.get("src/demo/resources/mistral-ai.txt"))
                        .purpose(PurposeType.ASSISTANTS)
                        .build())
                .join();
        this.fileId = file.getId();
        var vectorStore = openAI.vectorStores()
                .createAndPoll(VectorStoreRequest.builder()
                        .fileId(this.fileId)
                        .build());
        this.vectorStoreId = vectorStore.getId();
        this.responseId = null;
        this.responseIdList = new ArrayList<>();

        this.responseToolList = new ArrayList<>();
        this.responseToolList.addAll(FunctionResponseTool.functions(funcDefList));
        this.responseToolList.add(WebSearchResponseTool.builder()
                .searchContextSize(ContextSize.MEDIUM)
                .userLocation(Location.builder()
                        .country("PE")
                        .city("Lima")
                        .build())
                .build());
        this.responseToolList.add(FileSearchResponseTool.builder()
                .vectorStoreIds(List.of(this.vectorStoreId))
                .maxNumResults(2)
                .rankingOptions(RankingOption.builder()
                        .scoreThreshold(0.8)
                        .ranker(RankerType.AUTO)
                        .build())
                .build());
    }

    public void runConversation() {
        String myQuestion = "";
        while (true) {
            myQuestion = System.console().readLine(HINT);
            if (myQuestion.equalsIgnoreCase(EXIT)) {
                break;
            }
            var responseRequest = ResponseRequest.builder()
                    .instructions("You are a helpful assistant. Respond in a concise way.")
                    .input(myQuestion)
                    .tools(this.responseToolList)
                    .previousResponseId(this.responseId)
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
                case EventName.RESPONSE_CREATED:
                    var respCreated = (ResponseEvent) event.getData();
                    var response = respCreated.getResponse();
                    this.responseId = response.getId();
                    this.responseIdList.add(this.responseId);
                    break;
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
                    if (respOutItemAdded.getItem() instanceof FunctionCallItem) {
                        var funcCallItem = (FunctionCallItem) respOutItemAdded.getItem();
                        System.out.print("====> Function: " + funcCallItem.getName() + ". Arguments: ");
                    } else if (respOutItemAdded.getItem() instanceof WebSearchCallItem) {
                        System.out.println("====> WebSearch ...");
                    } else if (respOutItemAdded.getItem() instanceof FileSearchCallItem) {
                        System.out.println("====> FileSearch ...");
                    } else if (respOutItemAdded.getItem() instanceof OutputMessageItem) {
                        System.out.println("====> Message ...");
                    }
                    break;
                case EventName.RESPONSE_FUNCTION_CALL_ARGUMENTS_DELTA:
                    var respFuncCallArgsDelta = (ResponseFunctionCallArgsEvent) event.getData();
                    var argsDelta = respFuncCallArgsDelta.getArguments();
                    System.out.print(argsDelta);
                    break;
                case EventName.RESPONSE_FUNCTION_CALL_ARGUMENTS_DONE:
                    System.out.println();
                    break;
                case EventName.RESPONSE_OUTPUT_ITEM_DONE:
                    var respOutItemDone = (ResponseOutputItemEvent) event.getData();
                    if (respOutItemDone.getItem() instanceof FunctionCallItem) {
                        var functionCall = (FunctionCallItem) respOutItemDone.getItem();
                        var result = functionExecutor.execute(functionCall.getName(), functionCall.getArguments());
                        var responseRequest = ResponseRequest.builder()
                                .instructions("You are a helpful assistant. Respond in a concise way.")
                                .input(List.of(FunctionCallOutputItem.builder()
                                        .callId(functionCall.getCallId())
                                        .output(result.toString())
                                        .build()))
                                .tools(this.responseToolList)
                                .previousResponseId(this.responseId)
                                .model("gpt-4o-mini")
                                .temperature(0.1)
                                .build();
                        var funcResponseStream = this.openAI.responses().createStream(responseRequest).join();
                        handleResponseEvents(funcResponseStream);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    public void cleanConversation() {
        this.openAI.files().delete(this.fileId).join();
        this.openAI.vectorStores().delete(this.vectorStoreId).join();
        this.responseIdList.forEach(respId -> this.openAI.responses().delete(respId).join());
        this.openAI.shutDown();
    }

    public static void main(String[] args) {
        var demo = new ConversationV3Demo();
        demo.prepareConversation();
        demo.runConversation();
        demo.cleanConversation();
    }

}
