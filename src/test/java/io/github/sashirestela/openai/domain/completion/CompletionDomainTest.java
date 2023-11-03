package io.github.sashirestela.openai.domain.completion;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

class CompletionDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;
    static CompletionRequest completionRequest;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("Write a technical article about ChatGPT, no more than 100 words.")
                .suffix(null)
                .temperature(0.0)
                .maxTokens(300)
                .topP(1.0)
                .n(1)
                .logprobs(0)
                .echo(false)
                .stop(null)
                .presencePenalty(0.0)
                .frequencyPenalty(0.0)
                .bestOf(1)
                .logitBias(null)
                .user("test")
                .build();
    }

    @Test
    void testCompletionsCreateStream() throws IOException {
        DomainTestingHelper.get().mockForStream(httpClient, "src/test/resources/completions_create_stream.txt");
        var completionResponse = openAI.completions().createStream(completionRequest).join();
        completionResponse.filter(chatResp -> chatResp.firstText() != null)
                .map(chatResp -> chatResp.firstText())
                .forEach(System.out::print);
        assertNotNull(completionResponse);
    }

    @Test
    void testCompletionsCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/completions_create.json");
        var completionResponse = openAI.completions().create(completionRequest).join();
        System.out.println(completionResponse);
        assertNotNull(completionResponse);
    }
}