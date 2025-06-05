package io.github.sashirestela.openai.domain.response;

import io.github.sashirestela.cleverclient.client.JavaHttpClientAdapter;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.DomainTestingHelper.MockForType;
import io.github.sashirestela.openai.domain.assistant.RankingOption;
import io.github.sashirestela.openai.domain.assistant.RankingOption.RankerType;
import io.github.sashirestela.openai.domain.response.Input.Content.ImageInputContent;
import io.github.sashirestela.openai.domain.response.Input.Content.TextInputContent;
import io.github.sashirestela.openai.domain.response.Input.InputMessage;
import io.github.sashirestela.openai.domain.response.Input.Item;
import io.github.sashirestela.openai.domain.response.Input.Item.FunctionCallItem;
import io.github.sashirestela.openai.domain.response.Input.Item.FunctionCallOutputItem;
import io.github.sashirestela.openai.domain.response.Input.MessageRole;
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
import io.github.sashirestela.openai.domain.response.stream.ResponseOutputTextEvent;
import io.github.sashirestela.openai.test.utils.UtilSpecs;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ResponseDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .clientAdapter(new JavaHttpClientAdapter(httpClient))
                .build();
    }

    @Test
    void testResponseCreateBlocking() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_create.json");
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tutor in Maths.")
                .input("Explain me the Thales theorem in no more than 100 words.")
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateStreaming() throws IOException {
        DomainTestingHelper.get().mockForStream(httpClient, "src/test/resources/response_create_stream.txt");
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tutor in Maths.")
                .input("Explain me the Pythagorean theorem in no more than 100 words.")
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().createStream(responseRequest).join();
        responseResponse.forEach(e -> {
            switch (e.getName()) {
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
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateWithStruturedOutputs() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient, "src/test/resources/response_create_structuredoutputs.json");
        var responseRequest = ResponseRequest.builder()
                .input(List.of(
                        InputMessage.of(
                                "You are a helpful math tutor. Guide the user through the solution step by step.",
                                MessageRole.SYSTEM),
                        InputMessage.of("Explain me how can I solve 8x + 7 = -23", MessageRole.USER)))
                .text(ResponseText.jsonSchema(ResponseTextFormatJsonSchema.of(UtilSpecs.MathReasoning.class)))
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateWithFunctions() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/response_create_functions_1.json",
                                "src/test/resources/response_create_functions_2.json")));
        List<Object> inputs = new ArrayList<>();
        var funcDefList = Arrays.asList(
                FunctionDef.of(UtilSpecs.CurrentTemperature.class),
                FunctionDef.of(UtilSpecs.RainProbability.class));
        var functionExecutor = new FunctionExecutor(funcDefList);

        inputs.add(InputMessage.of("How's the temperature in Lima, Peru right now?", MessageRole.USER));
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tour guide.")
                .input(inputs)
                .tools(FunctionResponseTool.functions(funcDefList))
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
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
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateWithVision() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_create_vision.json");
        List<Object> inputs = new ArrayList<>();
        inputs.add(InputMessage.of(List.of(
                TextInputContent.of("What do you see in the image? Give in details in no more than 100 words."),
                ImageInputContent.of("https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg")),
                MessageRole.USER));
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tour guide.")
                .input(inputs)
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateWithWebSearch() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_create_websearch.json");
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a very up-to-date news assistant")
                .input("What is the most important news in Peruvian politics today?")
                .tool(WebSearchResponseTool.builder()
                        .searchContextSize(ContextSize.MEDIUM)
                        .userLocation(Location.builder()
                                .country("PE")
                                .city("Lima")
                                .build())
                        .build())
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateWithFileSearch() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_create_filesearch.json");
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a very seasoned assistant in AI topics")
                .input("What is the architecture of Mistral AI?")
                .tool(FileSearchResponseTool.builder()
                        .vectorStoreIds(List.of("vectore_store_id"))
                        .maxNumResults(2)
                        .rankingOptions(RankingOption.builder()
                                .scoreThreshold(0.8)
                                .ranker(RankerType.AUTO)
                                .build())
                        .build())
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateWithRemoteMcp() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_create_remote_mcp.json");
        var responseRequest = ResponseRequest.builder()
                .instructions("Answer questions using tools as needed")
                .input("What is the design philosophy of the sashirestela/cleverclient library?")
                .tool(McpResponseTool.builder()
                        .serverLabel("deepwiki")
                        .serverUrl("https://mcp.deepwiki.com/mcp")
                        .requireApproval(McpToolApprovalSetting.NEVER)
                        .allowedTools(List.of("ask_question"))
                        .build())
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateWithImageGeneration() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_create_imagegeneration.json");
        var responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful image generator")
                .input("Generate an image of orange cat hugging other white cat with a light blue scarf.")
                .tool(ImageGenerationResponseTool.builder()
                        .model("gpt-image-1")
                        .outputFormat(ImageFormat.JPEG)
                        .quality(ImageQuality.MEDIUM)
                        .size("1024x1024")
                        .build())
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(((Item.ImageGenerationCallItem) responseResponse.getOutput().get(0)).getResult());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateWithCodeInterpreter() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_create_codeinterpreter.json");
        var responseRequest = ResponseRequest.builder()
                .instructions(
                        "You are a personal math tutor. When asked a math question, write and run code to answer the question.")
                .input("I need to solve the equation 6x² + 5x - 6. Can you help me?")
                .tool(CodeInterpreterResponseTool.of(ContainerAuto.of()))
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_getone.json");
        var responseResponse = openAI.responses().getOne("responseId").join();
        System.out.println(responseResponse);
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseListInputItems() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_listinputitems.json");
        var inputItems = openAI.responses().getListInputItem("responseId").join();
        System.out.println(inputItems);
        assertNotNull(inputItems);
    }

    @Test
    void testResponseDelete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_delete.json");
        var deletedResponse = openAI.responses().delete("responseId").join();
        System.out.println(deletedResponse);
        assertNotNull(deletedResponse);
    }

}
