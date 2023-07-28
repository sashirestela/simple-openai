package io.github.sashirestela.openai;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.domain.model.ModelResponse;
import io.github.sashirestela.openai.service.ModelService;
import io.github.sashirestela.openai.support.Constant;
import io.github.sashirestela.openai.support.JsonUtil;
import io.github.sashirestela.openai.support.Url;

class ModelServiceImpl implements ModelService {
  private String apiKey;
  private HttpClient httpClient;

  public ModelServiceImpl(HttpClient httpClient, String apiKey) {
    this.httpClient = httpClient;
    this.apiKey = apiKey;
  }

	@Override
	public ModelResponse callModels() throws IOException, InterruptedException {
    HttpRequest httpRequest = HttpRequest.newBuilder()
      .uri(URI.create(Url.OPENAI_MODELS.value))
      .header(Constant.HEADER_AUTHORIZATION, Constant.AUTH_BEARER + apiKey)
      .GET()
      .build();
    HttpResponse<String> httpResponse = httpClient.send(
      httpRequest, BodyHandlers.ofString()
    );
    ModelResponse chatResponse = JsonUtil.one()
      .jsonToObject(httpResponse.body(), ModelResponse.class);
		return chatResponse;
	}

	@Override
	public Model callModel(String modelId) throws IOException, InterruptedException {
    HttpRequest httpRequest = HttpRequest.newBuilder()
      .uri(URI.create(Url.OPENAI_MODELS.value + "/" + modelId))
      .header(Constant.HEADER_AUTHORIZATION, Constant.AUTH_BEARER + apiKey)
      .GET()
      .build();
    HttpResponse<String> httpResponse = httpClient.send(
      httpRequest, BodyHandlers.ofString()
    );
    Model chatResponse = JsonUtil.one()
      .jsonToObject(httpResponse.body(), Model.class);
		return chatResponse;
	}
}