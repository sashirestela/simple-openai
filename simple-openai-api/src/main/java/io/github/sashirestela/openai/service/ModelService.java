package io.github.sashirestela.openai.service;

import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.domain.model.ModelResponse;
import io.github.sashirestela.openai.exception.SimpleUncheckedException;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.Path;

public interface ModelService {

  @GET("/v1/models")
  ModelResponse callModels() throws SimpleUncheckedException;

  @GET("/v1/models/{modelId}")
  Model callModel(@Path("modelId") String modelId) throws SimpleUncheckedException;

}