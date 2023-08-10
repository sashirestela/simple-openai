package io.github.sashirestela.openai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.sashirestela.openai.SimpleOpenAIApi;
import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.domain.model.ModelPermission;

@ExtendWith(MockitoExtension.class)
public class ModelServiceTest {

  @Mock
  HttpClient httpClient;
  @Mock
  HttpResponse<String> httpResponse;

  SimpleOpenAIApi openAIAPi;
  ModelService modelService;

  @BeforeEach
  void setupAll() {
    openAIAPi = new SimpleOpenAIApi("apiKey", httpClient);
    modelService = openAIAPi.createModelService();
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldReturnAModelWhenItExists() throws IOException, InterruptedException {
    String mockResponse = Files.readString(Path.of("src/test/resources/model.json"));
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(mockResponse);

    Model actualModel = modelService.callModel("gpt-3.5-turbo-16k-0613").join();
    Model expectedModel = new Model("gpt-3.5-turbo-16k-0613", "model", 1685474247, "openai",
        Arrays.asList(new ModelPermission("modelperm-0QpspTsmhLCWNC4Ypv9bFdNu", "model_permission", 1691186476, false,
            true, true, false, true, false, "*", null, false)),
        "gpt-3.5-turbo-16k-0613", null);
        assertEquals(expectedModel.getId(), actualModel.getId());
        assertEquals(expectedModel.getCreated(), actualModel.getCreated());
        assertEquals(expectedModel.getOwnedBy(), actualModel.getOwnedBy());
        assertEquals(expectedModel.getRoot(), actualModel.getRoot());
        assertEquals(expectedModel.getPermission().get(0).getId(), actualModel.getPermission().get(0).getId());
        assertEquals(expectedModel.getPermission().get(0).getCreated(), actualModel.getPermission().get(0).getCreated());
        assertEquals(expectedModel.getPermission().get(0).getOrganization(), actualModel.getPermission().get(0).getOrganization());
      }
}
