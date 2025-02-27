package io.github.sashirestela.openai.domain.moderation;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.moderation.ModerationRequest.MultiModalInput.ImageUrlInput;
import io.github.sashirestela.openai.domain.moderation.ModerationRequest.MultiModalInput.TextInput;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ModerationDomainTest {

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
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/moderations_create.json");
        var moderationRequest = ModerationRequest.builder()
                .input(Arrays.asList(
                        TextInput.of("I want to kill them."),
                        ImageUrlInput.of("https://upload.wikimedia.org/wikipedia/commons/e/e3/BWHammerSickle.jpg"),
                        TextInput.of("I am not sure what to think about them.")))
                .model("omni-moderation-latest")
                .build();
        var moderationResponse = openAI.moderations().create(moderationRequest).join();
        System.out.println(moderationResponse);
        assertNotNull(moderationResponse);
    }

}
