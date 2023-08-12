package io.github.sashirestela.openai.demo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.service.ModelService;

public class ModelServiceDemo extends AbstractDemo {

  private ModelService modelService;
  private String modelId;

  public ModelServiceDemo() {
    modelService = openAIApi.createModelService();
  }

  public void demoGetModels() {
    CompletableFuture<List<Model>> futureModels = modelService.callModels();
    List<Model> models = futureModels.join();
    models.forEach(model -> System.out.println(model));

    modelId = models.get(0).getId();
  }

  public void demoGetModel() {
    CompletableFuture<Model> futureModel = modelService.callModel(modelId);
    Model model = futureModel.join();
    System.out.println(model);
  }

  public static void main(String[] args) {
    ModelServiceDemo demo = new ModelServiceDemo();

    demo.addTitleAction("List of All Models", () -> demo.demoGetModels());
    demo.addTitleAction("First Model in List", () -> demo.demoGetModel());

    demo.run();
  }
}