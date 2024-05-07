package io.github.sashirestela.openai.demo;

public class ModelDemo extends AbstractDemo {

    private String modelId;

    public void demoGetModels() {
        var futureModels = openAI.models().getList();
        var models = futureModels.join();
        models.forEach(System.out::println);

        modelId = models.get(0).getId();
    }

    public void demoGetModel() {
        var futureModel = openAI.models().getOne(modelId);
        var model = futureModel.join();
        System.out.println(model);
    }

    @SuppressWarnings("unused")
    public void demoDeleteModel() {
        var futureModel = openAI.models().delete(modelId);
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
