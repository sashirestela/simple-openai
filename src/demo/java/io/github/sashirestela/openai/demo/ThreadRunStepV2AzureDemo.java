package io.github.sashirestela.openai.demo;

public class ThreadRunStepV2AzureDemo extends ThreadRunStepV2Demo {

    protected ThreadRunStepV2AzureDemo(String model) {
        super("azure", model);
        this.assistantProvider = this.openAIAzure;
    }

    public static void main(String[] args) {
        var demo = new ThreadRunStepV2AzureDemo("N/A");
        demo.prepareDemo();
        demo.addTitleAction("Demo ThreadRun v2 Create", demo::createThreadRun);
        demo.addTitleAction("Demo ThreadRunStep v2 List", demo::listThreadRunSteps);
        demo.addTitleAction("Demo ThreadRunStep v2 Retrieve", demo::retrieveThreadRunStep);
        demo.addTitleAction("Demo ThreadRunStep v2 Retrieve with Filters", demo::retrieveThreadRunStepWithFilters);
        demo.addTitleAction("Demo ThreadRun v2 Delete", demo::deleteDemo);
        demo.run();
    }

}
