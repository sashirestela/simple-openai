package io.github.sashirestela.openai.domain.completion;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

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
                .model("gpt-3.5-turbo-instruct")
                .prompt("Tell me the Pythagorean theorem in no more than 50 words.")
                .suffix(null)
                .temperature(0.0)
                .maxTokens(300)
                .topP(1.0)
                .n(1)
                .logprobs(0)
                .echo(false)
                .stop(null)
                .seed(1)
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
        completionResponse.forEach(responseChunk -> {
            var choices = responseChunk.getChoices();
            if (choices.size() > 0) {
                var delta = choices.get(0).getText();
                System.out.print(delta);
            }
            var usage = responseChunk.getUsage();
            if (usage != null) {
                System.out.println("\n");
                System.out.println(usage);
            }
        });
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
