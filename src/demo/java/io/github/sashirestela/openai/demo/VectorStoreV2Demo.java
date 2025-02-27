package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.assistant.ExpiresAfter;
import io.github.sashirestela.openai.domain.assistant.ExpiresAfter.Anchor;
import io.github.sashirestela.openai.domain.assistant.VectorStoreModifyRequest;
import io.github.sashirestela.openai.domain.assistant.VectorStoreRequest;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.service.AssistantServices;

import java.util.Map;

public class VectorStoreV2Demo extends AbstractDemo {

    private FileDemo fileDemo;
    private String fileId;
    private String vectorStoreId;

    protected AssistantServices assistantProvider;

    protected VectorStoreV2Demo() {
        this("standard", new FileDemo());
    }

    protected VectorStoreV2Demo(String provider, FileDemo fileDemo) {
        super(provider);
        this.assistantProvider = this.openAI;
        this.fileDemo = fileDemo;
    }

    public void prepareDemo() {
        var file = fileDemo.createFile("src/demo/resources/mistral-ai.txt", PurposeType.ASSISTANTS);
        fileId = file.getId();
    }

    public void createVectorStore() {
        var vectorStoreRequest = VectorStoreRequest.builder()
                .name("Demo Vector Store")
                .fileId(fileId)
                .expiresAfter(ExpiresAfter.builder()
                        .anchor(Anchor.LAST_ACTIVE_AT)
                        .days(1)
                        .build())
                .metadata(Map.of("env", "test"))
                .build();
        var vectorStore = assistantProvider.vectorStores().createAndPoll(vectorStoreRequest);
        System.out.println(vectorStore);
        vectorStoreId = vectorStore.getId();
    }

    public void modifyVectorStore() {
        var vectorStoreModifyRequest = VectorStoreModifyRequest.builder()
                .name("Demonstration Vector Store")
                .metadata(Map.of("env", "testing"))
                .metadata(Map.of("user", "mary"))
                .build();
        var vectorStore = assistantProvider.vectorStores().modify(vectorStoreId, vectorStoreModifyRequest).join();
        System.out.println(vectorStore);
    }

    public void retrieveVectorStore() {
        var vectorStore = assistantProvider.vectorStores().getOne(vectorStoreId).join();
        System.out.println(vectorStore);
    }

    public void listVectorStores() {
        var vectorStores = assistantProvider.vectorStores().getList().join();
        vectorStores.forEach(System.out::println);
    }

    public void deleteVectorStore() {
        var deletedFile = fileDemo.deleteFile(fileId);
        System.out.println(deletedFile);

        var deletedVectorStore = assistantProvider.vectorStores().delete(vectorStoreId).join();
        System.out.println(deletedVectorStore);
    }

    public static void main(String[] args) {
        var demo = new VectorStoreV2Demo();
        demo.prepareDemo();
        demo.addTitleAction("Demo VectorStore v2 Create", demo::createVectorStore);
        demo.addTitleAction("Demo VectorStore v2 Modify", demo::modifyVectorStore);
        demo.addTitleAction("Demo VectorStore v2 Retrieve", demo::retrieveVectorStore);
        demo.addTitleAction("Demo VectorStore v2 List", demo::listVectorStores);
        demo.addTitleAction("Demo VectorStore v2 Delete", demo::deleteVectorStore);
        demo.run();
    }

}
