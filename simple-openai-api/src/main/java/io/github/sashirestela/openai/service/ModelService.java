package io.github.sashirestela.openai.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.Path;

public interface ModelService {

  @GET("/v1/models")
  CompletableFuture<List<Model>> callModels();

  @GET("/v1/models/{modelId}")
  CompletableFuture<Model> callModel(@Path("modelId") String modelId);

}