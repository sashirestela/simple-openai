package io.github.sashirestela.openai.domain.finetuning;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

public class FineTuningDomainTest {

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
        .model("gpt-3.5-turbo-0613")
        .hyperParameters(new HyperParams(1))
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
    var fineTuningResponse = openAI.fineTunings().getEvents("finetuningId",2, null).join();
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
}