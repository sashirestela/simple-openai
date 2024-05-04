package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.assistant.AssistantModifyRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantResponseFormat;
import io.github.sashirestela.openai.domain.assistant.AssistantTool;

import java.util.Map;

public class AssistantV2Demo extends AbstractDemo {

    private String assistantId;

    public void createAssistant() {
        var assistantRequest = AssistantRequest.builder()
                .model("gpt-4-turbo")
                .name("Demo Assistant")
                .description("This is an assistant for demonstration purposes.")
                .instructions("You are a very kind assistant. If you cannot find correct facts to answer the "
                        + "questions, you have to refer to the attached files or use the functions provided. "
                        + "Finally, if you receive math questions, you must write and run code to answer them.")
                .tool(AssistantTool.FILE_SEARCH)
                .metadata(Map.of("user", "tester"))
                .temperature(0.2)
                .responseFormat("auto")
                .build();
        var assistant = openAI.assistants().create(assistantRequest).join();
        System.out.println(assistant);
        assistantId = assistant.getId();
    }

    public void modifyAssistant() {
        var assistantModifyRequest = AssistantModifyRequest.builder()
                .metadata(Map.of("env", "test"))
                .temperature(0.3)
                .responseFormat(AssistantResponseFormat.TEXT)
                .build();
        var assistant = openAI.assistants().modify(assistantId, assistantModifyRequest).join();
        System.out.println(assistant);
    }

    public void retrieveAssistant() {
        var assistant = openAI.assistants().getOne(assistantId).join();
        System.out.println(assistant);
    }

    public void listAssistants() {
        var assistants = openAI.assistants().getList().join();
        assistants.forEach(System.out::println);
    }

    public void deleteAssistant() {
        var deletedAssistant = openAI.assistants().delete(assistantId).join();
        System.out.println(deletedAssistant);
    }

    public static void main(String[] args) {
        var demo = new AssistantV2Demo();
        demo.addTitleAction("Demo Assistant v2 Create", demo::createAssistant);
        demo.addTitleAction("Demo Assistant v2 Modify", demo::modifyAssistant);
        demo.addTitleAction("Demo Assistant v2 Retrieve", demo::retrieveAssistant);
        demo.addTitleAction("Demo Assistant v2 List", demo::listAssistants);
        demo.addTitleAction("Demo Assistant v2 Delete", demo::deleteAssistant);
        demo.run();
    }

}
