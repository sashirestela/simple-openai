package io.github.sashirestela.openai.domain.batch;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.batch.BatchRequest.CompletionWindowType;
import io.github.sashirestela.openai.domain.batch.BatchRequest.EndpointType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class BatchDomainTest {

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
    void testBatchesCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/batches_create.json");
        var batchRequest = BatchRequest.builder()
                .inputFileId("file-pbNU1kkJTfCtM7FQBNNQ8PAW")
                .endpoint(EndpointType.CHAT_COMPLETIONS)
                .completionWindow(CompletionWindowType.T24H)
                .metadata(Map.of("key1", "value1"))
                .build();
        var batchResponse = openAI.batches().create(batchRequest).join();
        System.out.println(batchResponse);
        assertNotNull(batchResponse);
    }

    @Test
    void testBatchesGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/batches_getone.json");
        var batchResponse = openAI.batches().getOne("batch_kHnPT5OdGUU7Cha4Vj1RnM6i");
        System.out.println(batchResponse);
        assertNotNull(batchResponse);
    }

    @Test
    void testBatchesGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/batches_getlist.json");
        var batchResponse = openAI.batches().getList("batch_kHnPT5OdGUU7Cha4Vj1RnM6i", 3);
        System.out.println(batchResponse);
        assertNotNull(batchResponse);
    }

    @Test
    void testBatchesCancel() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/batches_cancel.json");
        var batchResponse = openAI.batches().cancel("batch_kHnPT5OdGUU7Cha4Vj1RnM6i");
        System.out.println(batchResponse);
        assertNotNull(batchResponse);
    }

}
