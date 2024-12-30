package io.github.sashirestela.openai.demo;

public class ModelMistralDemo extends ModelDemo {

    public ModelMistralDemo() {
        super("mistral");
        this.modelProvider = this.openAIMistral;
    }

    public static void main(String[] args) {
        var demo = new ModelMistralDemo();

        demo.addTitleAction("List of All Models", demo::demoGetModels);
        demo.addTitleAction("First Model in List", demo::demoGetModel);
        demo.addTitleAction("Trying to Delete a Model", demo::demoDeleteModel);

        demo.run();
    }

}
