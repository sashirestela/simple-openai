package io.github.sashirestela.openai.domain.finetuning;

import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.finetuning.Integration.IntegrationType;
import io.github.sashirestela.openai.domain.finetuning.Integration.WandbIntegration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

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
    void testFineTuningsCreateDpo() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/finetunings_create.json");
        var fineTuningRequest = FineTuningRequest.builder()
                .trainingFile("fileId")
                .validationFile("fileId")
                .model("gpt-3.5-turbo-1106")
                .suffix("suffix")
                .integration(Integration.builder()
                        .type(IntegrationType.WANDB)
                        .wandb(WandbIntegration.builder()
                                .project("my-wandb-project")
                                .name("ft-run-display-name")
                                .entity("testing")
                                .tag("first-experiment")
                                .tag("v2")
                                .build())
                        .build())
                .seed(99)
                .method(MethodFineTunning.dpo(HyperParams.builder()
                        .beta("auto")
                        .batchSize("auto")
                        .learningRateMultiplier("auto")
                        .nEpochs("auto")
                        .build()))
                .build();
        var fineTuningResponse = openAI.fineTunings().create(fineTuningRequest).join();
        System.out.println(fineTuningResponse);
        assertNotNull(fineTuningResponse);
    }

    @Test
    void testFineTuningsCreateSupervised() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/finetunings_create.json");
        var fineTuningRequest = FineTuningRequest.builder()
                .trainingFile("fileId")
                .validationFile("fileId")
                .model("gpt-3.5-turbo-1106")
                .suffix("suffix")
                .integration(Integration.builder()
                        .type(IntegrationType.WANDB)
                        .wandb(WandbIntegration.builder()
                                .project("my-wandb-project")
                                .name("ft-run-display-name")
                                .entity("testing")
                                .tag("first-experiment")
                                .tag("v2")
                                .build())
                        .build())
                .seed(99)
                .method(MethodFineTunning.supervised(HyperParams.builder()
                        .batchSize("auto")
                        .learningRateMultiplier("auto")
                        .nEpochs("auto")
                        .build()))
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
    void testFineTuningsGetCheckpoints() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/finetunings_getcheckpoints.json");
        var fineTuningResponse = openAI.fineTunings().getCheckpoints("finetuningId", 2, null).join();
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
    void testTranslateNumberEpochs() {
        var hyperParams = HyperParams.builder()
                .nEpochs(2)
                .build();
        var json = JsonUtil.objectToJson(hyperParams);
        System.out.println(json);
        assertTrue(json.contains("n_epochs"));
        assertFalse(json.contains("nepochs"));
    }

}
