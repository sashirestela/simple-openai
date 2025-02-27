package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.service.AssistantServices;

public class VectorStoreFileV2Demo extends AbstractDemo {

    private FileDemo fileDemo;
    private String fileId;
    private String vectorStoreId;

    protected AssistantServices assistantProvider;

    protected VectorStoreFileV2Demo() {
        this("standard", new FileDemo());
    }

    protected VectorStoreFileV2Demo(String provider, FileDemo fileDemo) {
        super(provider);
        this.assistantProvider = this.openAI;
        this.fileDemo = fileDemo;
    }

    public void prepareDemo() {
        var file = fileDemo.createFile("src/demo/resources/mistral-ai.txt", PurposeType.ASSISTANTS);
        fileId = file.getId();

        var vectorStore = assistantProvider.vectorStores().create().join();
        vectorStoreId = vectorStore.getId();

    }

    public void createVectorStoreFile() {
        var vectorStoreFile = assistantProvider.vectorStoreFiles().createAndPoll(vectorStoreId, fileId);
        System.out.println(vectorStoreFile);
    }

    public void retrieveVectorStoreFile() {
        var vectorStoreFile = assistantProvider.vectorStoreFiles().getOne(vectorStoreId, fileId).join();
        System.out.println(vectorStoreFile);
    }

    public void listVectorStoreFiles() {
        var vectorStoreFiles = assistantProvider.vectorStoreFiles().getList(vectorStoreId).join();
        vectorStoreFiles.forEach(System.out::println);
    }

    public void deletedVectorStoreFile() {
        var deletedFile = fileDemo.deleteFile(fileId);
        System.out.println(deletedFile);

        var deletedVectorStoreFile = assistantProvider.vectorStoreFiles().delete(vectorStoreId, fileId).join();
        System.out.println(deletedVectorStoreFile);

        var deletedVectorStore = assistantProvider.vectorStores().delete(vectorStoreId).join();
        System.out.println(deletedVectorStore);
    }

    public static void main(String[] args) {
        var demo = new VectorStoreFileV2Demo();
        demo.prepareDemo();
        demo.addTitleAction("Demo VectorStoreFile v2 Create", demo::createVectorStoreFile);
        demo.addTitleAction("Demo VectorStoreFile v2 Retrieve", demo::retrieveVectorStoreFile);
        demo.addTitleAction("Demo VectorStoreFile v2 List", demo::listVectorStoreFiles);
        demo.addTitleAction("Demo VectorStoreFile v2 Delete", demo::deletedVectorStoreFile);
        demo.run();
    }

}
