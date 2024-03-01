package io.github.sashirestela.openai.domain.completion;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void shouldCreateCompletionRequestWhenPromptIsRightClass() {
        Object[] testData = {
                "demo",
                List.of("first", "second"),
                List.of(1, 2, 3, 4),
                List.of(List.of(11, 22, 33))
        };
        for (Object data : testData) {
            var completionRequestBuilder = CompletionRequest.builder()
                    .model("model")
                    .prompt(data);
            assertDoesNotThrow(() -> completionRequestBuilder.build());
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingCompletionRequestWithPromptWrongClass() {
        Object[] testData = {
                1001,
                List.of(17.65, 23.68),
                List.of(List.of("first", "second"))
        };
        for (Object data : testData) {
            var completionRequestBuilder = CompletionRequest.builder()
                    .model("model")
                    .prompt(data);
            var exception = assertThrows(SimpleUncheckedException.class, () -> completionRequestBuilder.build());
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "The field prompt must be String or List<String> or List<Integer> or List<List<Integer>> classes.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }

    @Test
    void shouldCreateCompletionRequestWhenStopIsRightClass() {
        Object[] testData = {
                "stop",
                List.of("stop", "end", "quit", "finish")
        };
        for (Object data : testData) {
            var completionRequestBuilder = CompletionRequest.builder()
                    .model("model")
                    .prompt("prompt demo")
                    .stop(data);
            assertDoesNotThrow(() -> completionRequestBuilder.build());
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingCompletionRequestWithStopWrongClass() {
        Object[] testData = {
                1001,
                List.of(17.65, 23.68),
                List.of("one", "two", "three", "four", "five")
        };
        for (Object data : testData) {
            var completionRequestBuilder = CompletionRequest.builder()
                    .model("model")
                    .prompt("prompt demo")
                    .stop(data);
            var exception = assertThrows(SimpleUncheckedException.class, () -> completionRequestBuilder.build());
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "The field stop must be String or List<String> (max 4 items) classes.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }

}
