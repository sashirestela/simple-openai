package io.github.sashirestela.openai.domain.response;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ResponseDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;
    static ResponseRequest responseRequest;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();

        // Basic response request
        responseRequest = ResponseRequest.builder()
                .input("What is the capital of France?")
                .model("gpt-4o")
                .instructions("You are a helpful assistant.")
                .maxOutputTokens(1000)
                .stream(false)
                .temperature(0.7)
                .build();
    }

    @Test
    void testResponsesCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/responses_create.json");
        var response = openAI.responses().create(responseRequest).join();
        System.out.println(response);
        assertNotNull(response);
        assertTrue(response.getItems() != null);
    }

    @Test
    void testResponsesRetrieve() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/responses_create.json");
        var response = openAI.responses().retrieve("resp_abc123").join();
        System.out.println(response);
        assertNotNull(response);
        assertTrue(response.getItems() != null);
    }

    @Test
    void testResponsesDelete() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/deleted_object.json");
        var deletedObject = openAI.responses().delete("resp_abc123").join();
        System.out.println(deletedObject);
        assertNotNull(deletedObject);
        assertTrue(deletedObject.getDeleted());
    }

    @Test
    void testResponsesListInputItems() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/page.json");
        var page = openAI.responses().listInputItems("resp_abc123", null, null, null, 10).join();
        System.out.println(page);
        assertNotNull(page);
        assertTrue(page.getData() != null && !page.getData().isEmpty());

        // Verify the first item
        var firstItem = (Map<String, Object>) page.getData().get(0);
        assertNotNull(firstItem.get("id"));
        assertEquals("input_item", firstItem.get("object"));
    }

    private static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError("Expected " + expected + " but got " + actual);
    }

}
