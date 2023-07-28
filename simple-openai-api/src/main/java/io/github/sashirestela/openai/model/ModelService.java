package io.github.sashirestela.openai.model;

import java.io.IOException;

public interface ModelService {

  ModelResponse callModels()
    throws IOException, InterruptedException;

  Model callModel(String modelId)
    throws IOException, InterruptedException;

}