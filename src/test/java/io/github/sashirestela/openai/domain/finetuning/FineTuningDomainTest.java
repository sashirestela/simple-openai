package io.github.sashirestela.openai.domain.finetuning;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

class FineTuningDomainTest {

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
    void testFineTuningsCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/finetunings_create.json");
        var fineTuningRequest = FineTuningRequest.builder()
                .trainingFile("fileId")
                .validationFile("fileId")
                .model("gpt-3.5-turbo-1106")
                .hyperParameters(HyperParams.builder().batchSize("auto").learningRateMultiplier("auto")
                        .numberEpochs("auto").build())
                .suffix("suffix")
                .build();
        var fineTuningResponse = openAI.fineTunings().create(fineTuningRequest).join();
        System.out.println(fineTuningResponse);
        assertNotNull(fineTuningResponse);
    }

    @Test
    void testFineTuningsGetList() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/finetunings_getlist.json");
        var fineTuningResponse = openAI.fineTunings().getList(2, "finetuningId").join();
        System.out.println(fineTuningResponse);
        assertNotNull(fineTuningResponse);
    }

    @Test
    void testFineTuningsGetOne() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/finetunings_getone.json");
        var fineTuningResponse = openAI.fineTunings().getOne("finetuningId").join();
        System.out.println(fineTuningResponse);
        assertNotNull(fineTuningResponse);
    }

    @Test
    void testFineTuningsGetEvents() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/finetunings_getevents.json");
        var fineTuningResponse = openAI.fineTunings().getEvents("finetuningId", 2, null).join();
        System.out.println(fineTuningResponse);
        assertNotNull(fineTuningResponse);
    }

    @Test
    void testFineTuningsCancel() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/finetunings_cancel.json");
        var fineTuningResponse = openAI.fineTunings().cancel("finetuningId").join();
        System.out.println(fineTuningResponse);
        assertNotNull(fineTuningResponse);
    }

    @Test
    void shouldCreateHyperParamWhenBatchSizeIsRightClass() {
        Object[] testData = {
                1,
                "auto"
        };
        for (Object data : testData) {
            var builder = HyperParams.builder()
                    .batchSize(data)
                    .learningRateMultiplier("auto")
                    .numberEpochs("auto");
            assertDoesNotThrow(() -> builder.build());
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingHyperParamWithBatchSizeWrongClass() {
        Object[] testData = {
                1.5,
                false
        };
        for (Object data : testData) {
            var builder = HyperParams.builder()
                    .batchSize(data)
                    .learningRateMultiplier("auto")
                    .numberEpochs("auto");
            var exception = assertThrows(SimpleUncheckedException.class, () -> builder.build());
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "The field batchSize must be Integer or String classes.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }

    @Test
    void shouldCreateHyperParamWhenLearningRateMultiplierIsRightClass() {
        Object[] testData = {
                1.5,
                "auto"
        };
        for (Object data : testData) {
            var builder = HyperParams.builder()
                    .batchSize("auto")
                    .learningRateMultiplier(data)
                    .numberEpochs("auto");
            assertDoesNotThrow(() -> builder.build());
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingHyperParamWithLearningRateMultiplierWrongClass() {
        Object[] testData = {
                1,
                false
        };
        for (Object data : testData) {
            var builder = HyperParams.builder()
                    .batchSize("auto")
                    .learningRateMultiplier(data)
                    .numberEpochs("auto");
            var exception = assertThrows(SimpleUncheckedException.class, () -> builder.build());
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "The field learningRateMultiplier must be Double or String classes.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }

    @Test
    void shouldCreateHyperParamWhenNumberEpochsIsRightClass() {
        Object[] testData = {
                1,
                "auto"
        };
        for (Object data : testData) {
            var builder = HyperParams.builder()
                    .batchSize("auto")
                    .learningRateMultiplier("auto")
                    .numberEpochs(data);
            assertDoesNotThrow(() -> builder.build());
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingHyperParamWithNumberEpochsWrongClass() {
        Object[] testData = {
                1.5,
                false
        };
        for (Object data : testData) {
            var builder = HyperParams.builder()
                    .batchSize("auto")
                    .learningRateMultiplier("auto")
                    .numberEpochs(data);
            var exception = assertThrows(SimpleUncheckedException.class, () -> builder.build());
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "The field numberEpochs must be Integer or String classes.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }
}