package io.github.sashirestela.openai.demo;

public class AssistantV2AzureDemo extends AssistantV2Demo {

    protected AssistantV2AzureDemo(String model) {
        super("azure", model, new FileAzureDemo());
        this.assistantProvider = this.openAIAzure;
    }

    public static void main(String[] args) {
        var demo = new AssistantV2AzureDemo("N/A");
        demo.addTitleAction("Demo Assistant v2 Create", demo::createAssistant);
        demo.addTitleAction("Demo Assistant v2 Modify", demo::modifyAssistant);
        demo.addTitleAction("Demo Assistant v2 Retrieve", demo::retrieveAssistant);
        demo.addTitleAction("Demo Assistant v2 List", demo::listAssistants);
        demo.addTitleAction("Demo Assistant v2 Delete", demo::deleteAssistant);
        demo.run();
    }

}
