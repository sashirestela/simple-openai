package io.github.sashirestela.openai.demo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.OpenAIDeletedResponse;
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

  @SuppressWarnings("unused")
  public void demoDeleteModel() {
    CompletableFuture<OpenAIDeletedResponse> futureModel = openAI.models().delete(modelId);
    try {
      OpenAIDeletedResponse deleted = futureModel.join();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    ModelServiceDemo demo = new ModelServiceDemo();

    demo.addTitleAction("List of All Models", () -> demo.demoGetModels());
    demo.addTitleAction("First Model in List", () -> demo.demoGetModel());
    demo.addTitleAction("Trying to Delete a Model", () -> demo.demoDeleteModel());

    demo.run();
  }
}