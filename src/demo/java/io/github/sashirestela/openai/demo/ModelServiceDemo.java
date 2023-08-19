package io.github.sashirestela.openai.demo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.model.ModelResponse;

public class ModelServiceDemo extends AbstractDemo {

  private String modelId;

  public ModelServiceDemo() {
  }

  public void demoGetModels() {
    CompletableFuture<List<ModelResponse>> futureModels = openAI.models().getList();
    List<ModelResponse> models = futureModels.join();
    models.forEach(model -> System.out.println(model));

    modelId = models.get(0).getId();
  }

  public void demoGetModel() {
    CompletableFuture<ModelResponse> futureModel = openAI.models().getOne(modelId);
    ModelResponse model = futureModel.join();
    System.out.println(model);
  }

  public static void main(String[] args) {
    ModelServiceDemo demo = new ModelServiceDemo();

    demo.addTitleAction("List of All Models", () -> demo.demoGetModels());
    demo.addTitleAction("First Model in List", () -> demo.demoGetModel());

    demo.run();
  }
}