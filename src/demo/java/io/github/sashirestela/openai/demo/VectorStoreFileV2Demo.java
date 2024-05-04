package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;

public class VectorStoreFileV2Demo extends AbstractDemo {

    private FileServiceDemo fileDemo;
    private String fileId;
    private String vectorStoreId;

    public VectorStoreFileV2Demo() {
        fileDemo = new FileServiceDemo();
        var file = fileDemo.createFile("src/demo/resources/mistral-ai.txt", PurposeType.ASSISTANTS);
        fileId = file.getId();

        var vectorStore = openAI.vectorStores().create().join();
        vectorStoreId = vectorStore.getId();
    }

    public void createVectorStoreFile() {
        var vectorStoreFile = openAI.vectorStoreFiles().createAndPoll(vectorStoreId, fileId);
        System.out.println(vectorStoreFile);
    }

    public void retrieveVectorStoreFile() {
        var vectorStoreFile = openAI.vectorStoreFiles().getOne(vectorStoreId, fileId).join();
        System.out.println(vectorStoreFile);
    }

    public void listVectorStoreFiles() {
        var vectorStoreFiles = openAI.vectorStoreFiles().getList(vectorStoreId).join();
        vectorStoreFiles.forEach(System.out::println);
    }

    public void deletedVectorStoreFile() {
        var deletedFile = fileDemo.deleteFile(fileId);
        System.out.println(deletedFile);

        var deletedVectorStoreFile = openAI.vectorStoreFiles().delete(vectorStoreId, fileId).join();
        System.out.println(deletedVectorStoreFile);

        var deletedVectorStore = openAI.vectorStores().delete(vectorStoreId).join();
        System.out.println(deletedVectorStore);
    }

    public static void main(String[] args) {
        var demo = new VectorStoreFileV2Demo();
        demo.addTitleAction("Demo VectorStoreFile v2 Create", demo::createVectorStoreFile);
        demo.addTitleAction("Demo VectorStoreFile v2 Retrieve", demo::retrieveVectorStoreFile);
        demo.addTitleAction("Demo VectorStoreFile v2 List", demo::listVectorStoreFiles);
        demo.addTitleAction("Demo VectorStoreFile v2 Delete", demo::deletedVectorStoreFile);
        demo.run();
    }

}
