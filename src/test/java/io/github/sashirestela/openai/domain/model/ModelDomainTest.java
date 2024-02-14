package io.github.sashirestela.openai.domain.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import java.io.IOException;
import java.net.http.HttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ModelDomainTest {

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
    void testModelsGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/models_getlist.json");
        var modelResponse = openAI.models().getList().join();
        System.out.println(modelResponse);
        assertNotNull(modelResponse);
    }

    @Test
    void testModelsGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/models_getone.json");
        var modelResponse = openAI.models().getOne("modelId").join();
        System.out.println(modelResponse);
        assertNotNull(modelResponse);
    }

    @Test
    void testModelsDelete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/models_delete.json");
        var modelResponse = openAI.models().delete("modelId").join();
        System.out.println(modelResponse);
        assertNotNull(modelResponse);
    }
}