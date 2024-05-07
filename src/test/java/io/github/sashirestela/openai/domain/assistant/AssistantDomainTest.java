package io.github.sashirestela.openai.domain.assistant;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class AssistantDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
    }

    @Test
    void testCreateAssistant() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_create.json");
        var assistantRequest = AssistantRequest.builder()
                .model("gpt-4-turbo")
                .name("Demo Assistant")
                .description("This is an assistant for demonstration purposes.")
                .instructions("You are a very kind assistant. If you cannot find correct facts to answer the "
                        + "questions, you have to refer to the attached files or use the functions provided. "
                        + "Finally, if you receive math questions, you must write and run code to answer them.")
                .tool(AssistantTool.FILE_SEARCH)
                .metadata(Map.of("user", "tester"))
                .temperature(0.2)
                .responseFormat("auto")
                .build();
        var assistant = openAI.assistants().create(assistantRequest).join();
        System.out.println(assistant);
        assertNotNull(assistant);
    }

    @Test
    void testCreateMinimalAssistant() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_create.json");
        var assistant = openAI.assistants().create("gpt-4-turbo").join();
        System.out.println(assistant);
        assertNotNull(assistant);
    }

    @Test
    void testModifyAssistant() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_modify.json");
        var assistantModifyRequest = AssistantModifyRequest.builder()
                .metadata(Map.of("env", "test"))
                .temperature(0.3)
                .responseFormat(ResponseFormat.TEXT)
                .build();
        var assistant = openAI.assistants().modify("assistantId", assistantModifyRequest).join();
        System.out.println(assistant);
        assertNotNull(assistant);
    }

    @Test
    void testRetrieveAssistant() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_getone.json");
        var assistant = openAI.assistants().getOne("assistantId").join();
        System.out.println(assistant);
        assertNotNull(assistant);
    }

    @Test
    void testListAssistants() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_getlist.json");
        var assistants = openAI.assistants().getList().join();
        assistants.forEach(System.out::println);
        assertNotNull(assistants);
    }

    @Test
    void testDeleteAssistant() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/assistants_delete.json");
        var deletedAssistant = openAI.assistants().delete("assistantId").join();
        System.out.println(deletedAssistant);
        assertNotNull(deletedAssistant);
    }

}
