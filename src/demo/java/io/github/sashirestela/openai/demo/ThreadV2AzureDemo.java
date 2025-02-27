package io.github.sashirestela.openai.demo;

public class ThreadV2AzureDemo extends ThreadV2Demo {

    protected ThreadV2AzureDemo(String model) {
        super("azure", model, new FileAzureDemo());
        this.assistantProvider = this.openAIAzure;
    }

    public static void main(String[] args) {
        var demo = new ThreadV2AzureDemo("N/A");
        demo.prepareDemo();
        demo.addTitleAction("Demo Thread v2 Create", demo::createThread);
        demo.addTitleAction("Demo Thread v2 Modify", demo::modifyThread);
        demo.addTitleAction("Demo Thread v2 Retrieve", demo::retrieveThread);
        demo.addTitleAction("Demo Thread v2 Delete", demo::deleteThread);
        demo.run();
    }

}
