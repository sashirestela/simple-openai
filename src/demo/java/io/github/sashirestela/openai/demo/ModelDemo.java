package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.service.ModelServices;

public class ModelDemo extends AbstractDemo {

    protected String modelId;
    protected ModelServices modelProvider;

    public ModelDemo() {
        this("standard");
    }

    protected ModelDemo(String provider) {
        super(provider);
        this.modelProvider = this.openAI;
    }

    public void demoGetModels() {
        var futureModels = modelProvider.models().getList();
        var models = futureModels.join();
        models.forEach(System.out::println);

        modelId = models.get(0).getId();
    }

    public void demoGetModel() {
        var futureModel = modelProvider.models().getOne(modelId);
        var model = futureModel.join();
        System.out.println(model);
    }

    @SuppressWarnings("unused")
    public void demoDeleteModel() {
        var futureModel = modelProvider.models().delete(modelId);
        try {
            var deleted = futureModel.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        var demo = new ModelDemo();

        demo.addTitleAction("List of All Models", demo::demoGetModels);
        demo.addTitleAction("First Model in List", demo::demoGetModel);
        demo.addTitleAction("Trying to Delete a Model", demo::demoDeleteModel);

        demo.run();
    }

}
