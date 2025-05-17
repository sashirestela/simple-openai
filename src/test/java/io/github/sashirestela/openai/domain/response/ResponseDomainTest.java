package io.github.sashirestela.openai.domain.response;

import io.github.sashirestela.cleverclient.client.JavaHttpClientAdapter;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.response.stream.EventName;
import io.github.sashirestela.openai.domain.response.stream.ResponseOutputTextEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
                .clientAdapter(new JavaHttpClientAdapter(httpClient))
                .build();
        responseRequest = ResponseRequest.builder()
                .instructions("You are a helpful tutor in Maths.")
                .input("Explain me the Euclides Theorem in no more than 100 words.")
                .model("gpt-4o-mini")
                .temperature(0.1)
                .build();
    }

    @Test
    void testResponseCreateBlocking() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/response_create.json");
        var responseResponse = openAI.responses().create(responseRequest).join();
        System.out.println(responseResponse.outputText());
        assertNotNull(responseResponse);
    }

    @Test
    void testResponseCreateStreaming() throws IOException {
        DomainTestingHelper.get().mockForStream(httpClient, "src/test/resources/response_create_stream.txt");
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
