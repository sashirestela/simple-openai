package io.github.sashirestela.openai.service;

import java.io.IOException;

import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.domain.model.ModelResponse;

public interface ModelService {

  ModelResponse callModels()
    throws IOException, InterruptedException;

  Model callModel(String modelId)
    throws IOException, InterruptedException;

}