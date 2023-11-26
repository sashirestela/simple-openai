package io.github.sashirestela.openai.domain.assistant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.function.Functional;

public class AssistantDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;
    static AssistantRequest assistantRequest;
    static String assistantId;
    static String fileId;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        assistantRequest = AssistantRequest.builder()
                .model("gpt-4-1106-preview")
                .name("Math Tutor")
                .description("Assistant for mathematics topics")
                .instructions("You are a personal math tutor. Use the added function if necessary or the added files.")
                .tool(AssistantTool.builder().type("function")
                        .function(AssistantFunction.function(ChatFunction.builder().name("product")
                                .description("Get the product of two numbers")
                                .functionalClass(Product.class).build()))
                        .build())
                .tool(AssistantTool.RETRIEVAL)
                .fileId(fileId)
                .metadata(Map.of("phase", "test"))
                .build();
        assistantId = "asst_HVW5yeBxNVvEMHF5uoFspA2R";
        fileId = "file-bI6iyBmUO1jOvZVTd3wLzjFq";
    }

    @Test
    void testAssistantsCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants__create.json");
        var response = openAI.assistants().create(assistantRequest).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testAssistantsModify() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants__modify.json");
        var response = openAI.assistants().modify(assistantId, assistantRequest).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testAssistantsGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants__getone.json");
        var response = openAI.assistants().getOne(assistantId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testAssistantsGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants__getlist.json");
        var response = openAI.assistants().getList().join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testAssistantsDelete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants__delete.json");
        var response = openAI.assistants().delete(assistantId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testAssistantMutable() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants__getone.json");
        var assistant = openAI.assistants().getOne(assistantId).join();
        var newAssistant = assistant.mutate().name("Math Expert").build();
        assertEquals("Math Expert", newAssistant.getName());
    }

    @Test
    void testAssistantsFilesCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_files_create.json");
        var response = openAI.assistants().createFile(assistantId, fileId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testAssistantsFilesGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_files_getone.json");
        var response = openAI.assistants().getFile(assistantId, fileId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testAssistantsFilesGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_files_getlist.json");
        var response = openAI.assistants().getFileList(assistantId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    @Test
    void testAssistantsFilesDelete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_files_delete.json");
        var response = openAI.assistants().deleteFile(assistantId, fileId).join();
        System.out.println(response);
        assertNotNull(response);
    }

    static class Product implements Functional {
        @JsonPropertyDescription("The multiplicand part of a product")
        @JsonProperty(required = true)
        public double multiplicand;

        @JsonPropertyDescription("The multiplier part of a product")
        @JsonProperty(required = true)
        public double multiplier;

        @Override
        public Object execute() {
            return multiplicand * multiplier;
        }
    }
}