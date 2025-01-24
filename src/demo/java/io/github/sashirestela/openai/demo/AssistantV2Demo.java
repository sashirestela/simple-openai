package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.assistant.AssistantModifyRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantTool;
import io.github.sashirestela.openai.domain.assistant.ChunkingStrategy;
import io.github.sashirestela.openai.domain.assistant.ChunkingStrategy.StaticChunking;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull.FileSearch;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull.FileSearch.VectorStore;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;

import java.util.Map;

public class AssistantV2Demo extends AbstractDemo {

    private FileDemo fileDemo;
    private String fileId;
    private String assistantId;

    public AssistantV2Demo() {
        fileDemo = new FileDemo();
        var file = fileDemo.createFile("src/demo/resources/mistral-ai.txt", PurposeType.ASSISTANTS);
        fileId = file.getId();
    }

    public void createAssistant() {
        var assistantRequest = AssistantRequest.builder()
                .model("gpt-4o")
                .name("Demo Assistant")
                .description("This is an assistant for demonstration purposes.")
                .instructions("You are a very kind assistant. If you cannot find correct facts to answer the "
                        + "questions, you have to refer to the attached files or use the functions provided. "
                        + "Finally, if you receive math questions, you must write and run code to answer them.")
                .tool(AssistantTool.fileSearch(10))
                .toolResources(ToolResourceFull.builder()
                        .fileSearch(FileSearch.builder()
                                .vectorStore(VectorStore.builder()
                                        .fileId(fileId)
                                        .chunkingStrategy(ChunkingStrategy.staticType(StaticChunking.builder()
                                                .maxChunkSizeTokens(100)
                                                .chunkOverlapTokens(50)
                                                .build()))
                                        .build())
                                .build())
                        .build())
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
                .responseFormat(ResponseFormat.TEXT)
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
        var assistant = openAI.assistants().getOne(assistantId).join();
        var vectorStoreId = assistant.getToolResources().getFileSearch().getVectorStoreIds().get(0);

        var deletedFile = fileDemo.deleteFile(fileId);
        System.out.println(deletedFile);

        var deletedVectorStore = openAI.vectorStores().delete(vectorStoreId).join();
        System.out.println(deletedVectorStore);

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
