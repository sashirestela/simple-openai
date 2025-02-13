package io.github.sashirestela.openai.demo;

public class ModelDeepseekDemo extends ModelDemo {

    public ModelDeepseekDemo() {
        super("deepseek");
        this.modelProvider = this.openAIDeepseek;
    }

    public static void main(String[] args) {
        var demo = new ModelDeepseekDemo();

        demo.addTitleAction("List of All Models", demo::demoGetModels);

        demo.run();
    }

}
