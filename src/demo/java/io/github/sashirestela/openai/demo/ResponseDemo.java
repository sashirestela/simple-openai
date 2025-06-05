package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.demo.util.UtilSpecs;
import io.github.sashirestela.openai.domain.assistant.RankingOption;
import io.github.sashirestela.openai.domain.assistant.RankingOption.RankerType;
import io.github.sashirestela.openai.domain.assistant.VectorStoreRequest;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.domain.response.Input.Content.ImageInputContent;
import io.github.sashirestela.openai.domain.response.Input.Content.TextInputContent;
import io.github.sashirestela.openai.domain.response.Input.InputMessage;
import io.github.sashirestela.openai.domain.response.Input.Item;
import io.github.sashirestela.openai.domain.response.Input.Item.FunctionCallItem;
import io.github.sashirestela.openai.domain.response.Input.Item.FunctionCallOutputItem;
import io.github.sashirestela.openai.domain.response.Input.MessageRole;
import io.github.sashirestela.openai.domain.response.ResponseRequest;
import io.github.sashirestela.openai.domain.response.ResponseText;
import io.github.sashirestela.openai.domain.response.ResponseText.ResponseTextFormat.ResponseTextFormatJsonSchema;
import io.github.sashirestela.openai.domain.response.ResponseTool.CodeInterpreterResponseTool;
import io.github.sashirestela.openai.domain.response.ResponseTool.ContainerAuto;
import io.github.sashirestela.openai.domain.response.ResponseTool.ContextSize;
import io.github.sashirestela.openai.domain.response.ResponseTool.FileSearchResponseTool;
import io.github.sashirestela.openai.domain.response.ResponseTool.FunctionResponseTool;
import io.github.sashirestela.openai.domain.response.ResponseTool.ImageFormat;
import io.github.sashirestela.openai.domain.response.ResponseTool.ImageGenerationResponseTool;
import io.github.sashirestela.openai.domain.response.ResponseTool.ImageQuality;
import io.github.sashirestela.openai.domain.response.ResponseTool.Location;
import io.github.sashirestela.openai.domain.response.ResponseTool.McpResponseTool;
import io.github.sashirestela.openai.domain.response.ResponseTool.McpToolApprovalSetting;
import io.github.sashirestela.openai.domain.response.ResponseTool.WebSearchResponseTool;
import io.github.sashirestela.openai.domain.response.stream.EventName;
import io.github.sashirestela.openai.domain.response.stream.ResponseEvent;
import io.github.sashirestela.openai.domain.response.stream.ResponseOutputTextEvent;
import io.github.sashirestela.openai.service.ResponseServices;
import io.github.sashirestela.openai.support.Base64Util;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseDemo extends AbstractDemo {

    protected String model;
    protected ResponseServices responseProvider;

    private List<String> responseIdList;

    protected ResponseDemo(String model) {
        this("standard", model);
    }

    protected ResponseDemo(String provider, String model) {
        super(provider);
        this.model = model;
        this.responseIdList = new ArrayList<>();
        this.responseProvider = this.openAI;
    }

    public void createResponseBlocking() {
        var question = "Explain me the Thales theorem in no more than 100 words.";
        System.out.println("Question:\n" + question);
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tutor in Maths.")
                .input(question)
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Answer:");
        System.out.println(responseResponse.outputText());
    }

    public void createResponseStreaming() {
        var question = "Explain me the Pythagorean theorem in no more than 100 words.";
        System.out.println("Question:\n" + question);
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tutor in Maths.")
                .input(question)
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().createStream(responseRequest).join();
        System.out.println("Answer:");
        responseResponse.forEach(e -> {
            switch (e.getName()) {
                case EventName.RESPONSE_CREATED:
                    var responseEvent = (ResponseEvent) e.getData();
                    var response = responseEvent.getResponse();
                    this.responseIdList.add(response.getId());
                    break;
                case EventName.RESPONSE_OUTPUT_TEXT_DELTA:
                    var responseOutputTextEvent = (ResponseOutputTextEvent) e.getData();
                    var delta = responseOutputTextEvent.getText();
                    System.out.print(delta);
                    break;
                case EventName.RESPONSE_OUTPUT_TEXT_DONE:
                    System.out.println();
                    break;
                default:
                    break;
            }
        });
    }

    public void createResponseWithStructuredOutputs() {
        var question = "Explain me how can I solve 8x + 7 = -23";
        System.out.println("Question:\n" + question);
        var responseRequest = ResponseRequest.builder()
                .input(List.of(
                        InputMessage.of(
                                "You are a helpful math tutor. Guide the user through the solution step by step.",
                                MessageRole.SYSTEM),
                        InputMessage.of(question, MessageRole.USER)))
                .text(ResponseText.jsonSchema(ResponseTextFormatJsonSchema.of(UtilSpecs.MathReasoning.class)))
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Answer:");
        System.out.println(responseResponse.outputText());
    }

    public void createResponseWithFunctions() {
        List<Object> inputs = new ArrayList<>();
        var funcDefList = Arrays.asList(
                FunctionDef.of(UtilSpecs.CurrentTemperature.class),
                FunctionDef.of(UtilSpecs.RainProbability.class));
        var functionExecutor = new FunctionExecutor(funcDefList);

        var question = "How's the temperature in Lima, Peru right now?";
        System.out.println("Question:\n" + question);
        inputs.add(InputMessage.of(question, MessageRole.USER));
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tour guide.")
                .input(inputs)
                .tools(FunctionResponseTool.functions(funcDefList))
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        var functionCall = (FunctionCallItem) responseResponse.getOutput().get(0);
        inputs.add(functionCall);
        var result = functionExecutor.execute(functionCall.getName(), functionCall.getArguments());
        inputs.add(FunctionCallOutputItem.builder()
                .callId(functionCall.getCallId())
                .output(result.toString())
                .build());
        responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tour guide.")
                .input(inputs)
                .tools(FunctionResponseTool.functions(funcDefList))
                .model(this.model)
                .temperature(0.1)
                .build();
        responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Answer:");
        System.out.println(responseResponse.outputText());
    }

    public void createResponseWithVision() {
        List<Object> inputs = new ArrayList<>();
        var question = "What do you see in the image? Give in details in no more than 100 words.";
        System.out.println("Question:\n" + question);
        inputs.add(InputMessage.of(List.of(
                TextInputContent.of(question),
                ImageInputContent.of("https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg")),
                MessageRole.USER));
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tour guide.")
                .input(inputs)
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Answer:");
        System.out.println(responseResponse.outputText());
    }

    public void createResponseWebSearch() {
        var question = "What is the most important news in Peruvian politics today?";
        System.out.println("Question:\n" + question);
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a very up-to-date news assistant")
                .input(question)
                .tool(WebSearchResponseTool.builder()
                        .searchContextSize(ContextSize.MEDIUM)
                        .userLocation(Location.builder()
                                .country("PE")
                                .city("Lima")
                                .build())
                        .build())
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Answer:");
        System.out.println(responseResponse.outputText());
    }

    public void createResponseFileSearch() {
        var aiProvider = this.openAI;
        var file = aiProvider.files()
                .create(FileRequest.builder()
                        .file(Paths.get("src/demo/resources/mistral-ai.txt"))
                        .purpose(PurposeType.ASSISTANTS)
                        .build())
                .join();
        var vectorStore = aiProvider.vectorStores()
                .createAndPoll(VectorStoreRequest.builder()
                        .fileId(file.getId())
                        .build());

        var question = "What is the architecture of Mistral AI?";
        System.out.println("Question:\n" + question);
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a very seasoned assistant in AI topics")
                .input(question)
                .tool(FileSearchResponseTool.builder()
                        .vectorStoreIds(List.of(vectorStore.getId()))
                        .maxNumResults(2)
                        .rankingOptions(RankingOption.builder()
                                .scoreThreshold(0.8)
                                .ranker(RankerType.AUTO)
                                .build())
                        .build())
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Answer:");
        System.out.println(responseResponse.outputText());

        aiProvider.vectorStores().delete(vectorStore.getId()).join();
        aiProvider.files().delete(file.getId()).join();
    }

    public void createResponseRemoteMcp() {
        var question = "What is the design philosophy of the sashirestela/cleverclient library?";
        System.out.println("Question:\n" + question);
        var responseRequest = ResponseRequest.builder()
                .instructions("Answer questions using tools as needed")
                .input(question)
                .tool(McpResponseTool.builder()
                        .serverLabel("deepwiki")
                        .serverUrl("https://mcp.deepwiki.com/mcp")
                        .requireApproval(McpToolApprovalSetting.NEVER)
                        .allowedTools(List.of("ask_question"))
                        .build())
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Answer:");
        System.out.println(responseResponse.outputText());
    }

    public void createResponseImageGeneration() {
        var question = "Generate an image of orange cat hugging other white cat with a light blue scarf.";
        System.out.println("Question:\n" + question);
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful image generator")
                .input(question)
                .tool(ImageGenerationResponseTool.builder()
                        .model("gpt-image-1")
                        .outputFormat(ImageFormat.JPEG)
                        .quality(ImageQuality.MEDIUM)
                        .size("1024x1024")
                        .build())
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Image generated in:");
        var imageData = responseResponse.getOutput()
                .stream()
                .filter(item -> (item instanceof Item.ImageGenerationCallItem))
                .map(item -> ((Item.ImageGenerationCallItem) item).getResult())
                .collect(Collectors.joining());
        var filePath = "src/demo/resources/cats.jpeg";
        Base64Util.decode(imageData, filePath);
        System.out.println(filePath);
    }

    public void createResponseCodeInterpreter() {
        var question = "I need to solve the equation 6x² + 5x - 6. Can you help me?";
        System.out.println("Question:\n" + question);
        var responseRequest = ResponseRequest.builder()
                .instructions(
                        "You are a personal math tutor. When asked a math question, write and run code to answer the question.")
                .input(question)
                .tool(CodeInterpreterResponseTool.of(ContainerAuto.of()))
                .model(this.model)
                .temperature(0.1)
                .build();
        var responseResponse = responseProvider.responses().create(responseRequest).join();
        this.responseIdList.add(responseResponse.getId());
        System.out.println("Answer:");
        System.out.println(responseResponse.outputText());
    }

    public void getResponse() {
        var responseResponse = responseProvider.responses().getOne(responseIdList.get(0)).join();
        System.out.println(responseResponse);
    }

    public void listInputItems() {
        var inputItems = responseProvider.responses().getListInputItem(responseIdList.get(0)).join();
        inputItems.getData().forEach(System.out::println);
    }

    public void deleteResponse() {
        this.responseIdList.forEach(responseId -> {
            var deletedResponse = responseProvider.responses().delete(responseId).join();
            System.out.println(deletedResponse);
        });
    }

    public static void main(String[] args) {
        var demo = new ResponseDemo("gpt-4o-mini");
        demo.addTitleAction("Demo Response Create (Blocking)", demo::createResponseBlocking);
        demo.addTitleAction("Demo Response Create (Streaming)", demo::createResponseStreaming);
        demo.addTitleAction("Demo Response Create with StructuredOutputs", demo::createResponseWithStructuredOutputs);
        demo.addTitleAction("Demo Response Create with Functions", demo::createResponseWithFunctions);
        demo.addTitleAction("Demo Response Create with Vision", demo::createResponseWithVision);
        demo.addTitleAction("Demo Response Create with WebSearch", demo::createResponseWebSearch);
        demo.addTitleAction("Demo Response Create with FileSearch", demo::createResponseFileSearch);
        demo.addTitleAction("Demo Response Create with RemoteMcp", demo::createResponseRemoteMcp);
        demo.addTitleAction("Demo Response Create with ImageGeneration", demo::createResponseImageGeneration);
        demo.addTitleAction("Demo Response Create with CodeInterpreter", demo::createResponseCodeInterpreter);
        demo.addTitleAction("Demo Response GetOne ", demo::getResponse);
        demo.addTitleAction("Demo Response List InputItems", demo::listInputItems);
        demo.addTitleAction("Demo Response Delete", demo::deleteResponse);
        demo.run();
    }

}
