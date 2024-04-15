package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAIAzure;

public class AssistantAzureServiceDemo extends BaseAssistantServiceDemo {

    public AssistantAzureServiceDemo(String baseUrl, String apiKey, String apiVersion) {
        super(SimpleOpenAIAzure.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .apiVersion(apiVersion)
                .build(), "N/A");
    }

    public static void main(String[] args) {
        var baseUrl = System.getenv("AZURE_OPENAI_BASE_URL");
        var apiKey = System.getenv("AZURE_OPENAI_API_KEY");
        var apiVersion = System.getenv("AZURE_OPENAI_API_VERSION");

        var demo = new AssistantAzureServiceDemo(baseUrl, apiKey, apiVersion);

        demo.addTitleAction("Demo Call Assistant Create", demo::demoCreateAssistant);
        demo.addTitleAction("Demo Call Assistant Retrieve and Modify", demo::demoRetrieveAndModifyAssistant);
        demo.addTitleAction("Demo Call Assistant List", demo::demoListAssistants);
        demo.addTitleAction("Demo Call Assistant File Upload", demo::demoUploadAssistantFile);
        demo.addTitleAction("Demo Call Assistant Thread Create", demo::demoCreateThread);
        demo.addTitleAction("Demo Call Assistant Thread Run", demo::demoRunThreadAndWaitUntilComplete);
        demo.addTitleAction("Demo Call Assistant Messages Get", demo::demoGetAssistantMessages);
        demo.addTitleAction("Demo Call Assistant Delete", demo::demoDeleteAssistantAndThread);

        demo.run();
    }

}
