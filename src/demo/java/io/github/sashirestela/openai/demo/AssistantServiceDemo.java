package io.github.sashirestela.openai.demo;

public class AssistantServiceDemo extends BaseAssistantServiceDemo {

    public AssistantServiceDemo(String modelId) {
        super(modelId);
    }

    public static void main(String[] args) {
        var demo = new AssistantServiceDemo("gpt-3.5-turbo-1106");

        demo.addTitleAction("Demo Call Assistant Create", demo::demoCreateAssistant);
        demo.addTitleAction("Demo Call Assistant Retrieve and Modify", demo::demoRetrieveAndModifyAssistant);
        demo.addTitleAction("Demo Call Assistant List", demo::demoListAssistants);
        demo.addTitleAction("Demo Call Assistant File Upload", demo::demoUploadAssistantFile);
        demo.addTitleAction("Demo Call Assistant Thread Create", demo::demoCreateThread);
        demo.addTitleAction("Demo Call Assistant Thread Run", demo::demoRunThreadAndWaitUntilComplete);
        demo.addTitleAction("Demo Call Assistant Thread Run Stream", demo::demoRunThreadAndStream);
        demo.addTitleAction("Demo Call Assistant Messages Get", demo::demoGetAssistantMessages);
        demo.addTitleAction("Demo Call Assistant & Thread Delete", demo::demoDeleteAssistantAndThread);

        demo.run();
    }

}
