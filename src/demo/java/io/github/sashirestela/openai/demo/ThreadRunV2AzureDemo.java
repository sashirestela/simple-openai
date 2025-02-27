package io.github.sashirestela.openai.demo;

public class ThreadRunV2AzureDemo extends ThreadRunV2Demo {

    protected ThreadRunV2AzureDemo(String model) {
        super("azure", model, new FileAzureDemo());
        this.assistantProvider = this.openAIAzure;
    }

    public static void main(String[] args) {
        var demo = new ThreadRunV2AzureDemo("N/A");
        demo.prepareDemo();
        demo.addTitleAction("Demo ThreadRun v2 Create", demo::createThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Create Stream", demo::createThreadRunStream);
        demo.addTitleAction("Demo ThreadRun v2 Submit Tool Output", demo::submitToolOutputToThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Submit Tool Output Stream", demo::submitToolOutputToThreadRunStream);
        demo.addTitleAction("Demo ThreadRun v2 Create Thread and Run", demo::createThreadAndThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Create Thread and Run Stream", demo::createThreadAndThreadRunStream);
        demo.addTitleAction("Demo ThreadRun v2 Modify", demo::modifyThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Retrieve", demo::retrieveThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 List", demo::listThreadRuns);
        demo.addTitleAction("Demo ThreadRun v2 Delete", demo::deleteDemo);
        demo.run();
    }

}
