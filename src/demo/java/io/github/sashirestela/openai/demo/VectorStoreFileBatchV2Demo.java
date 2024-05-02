package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.file.PurposeType;

import java.util.ArrayList;
import java.util.List;

public class VectorStoreFileBatchV2Demo extends AbstractDemo {

    private FileServiceDemo fileDemo;
    private List<String> fileIdList;
    private String vectorStoreId;
    private String vectorStoreFileBatchId;

    public VectorStoreFileBatchV2Demo() {
        fileIdList = new ArrayList<>();
        fileDemo = new FileServiceDemo();
        for (int i = 0; i < 2; i++) {
            var file = fileDemo.createFile("src/demo/resources/mistral-ai.txt",
                    PurposeType.ASSISTANTS);
            fileIdList.add(file.getId());
        }
        var vectorStore = openAI.vectorStores().create().join();
        vectorStoreId = vectorStore.getId();
    }

    public void createVectorStoreFileBatch() {
        var vectorStoreFileBatch = openAI.vectorStoreFileBatches().createAndPoll(vectorStoreId, fileIdList);
        vectorStoreFileBatchId = vectorStoreFileBatch.getId();
        System.out.println(vectorStoreFileBatch);
    }

    public void retrieveVectorStoreFileBatch() {
        var vectorStoreFileBatch = openAI.vectorStoreFileBatches().getOne(vectorStoreId, vectorStoreFileBatchId).join();
        System.out.println(vectorStoreFileBatch);
    }

    public void listVectorStoreFilesInBatch() {
        var vectorStoreFiles = openAI.vectorStoreFileBatches().getFiles(vectorStoreId, vectorStoreFileBatchId).join();
        vectorStoreFiles.forEach(System.out::println);
    }

    public void cancelVectorStoreFileBatch() {
        var vectorStoreFileBatch = openAI.vectorStoreFileBatches().create(vectorStoreId, fileIdList).join();
        var newVectorStoreFileBatchId = vectorStoreFileBatch.getId();
        vectorStoreFileBatch = openAI.vectorStoreFileBatches().cancel(vectorStoreId, newVectorStoreFileBatchId).join();
        System.out.println(vectorStoreFileBatch);
    }

    public void deletedVectorStoreFiles() {
        for (String fileId : fileIdList) {
            var deletedFile = fileDemo.deleteFile(fileId);
            System.out.println(deletedFile);

            var deletedVectorStoreFile = openAI.vectorStoreFiles().delete(vectorStoreId, fileId).join();
            System.out.println(deletedVectorStoreFile);
        }
        var deletedVectorStore = openAI.vectorStores().delete(vectorStoreId).join();
        System.out.println(deletedVectorStore);
    }

    public static void main(String[] args) {
        var demo = new VectorStoreFileBatchV2Demo();
        demo.addTitleAction("Demo VectorStoreFileBatch v2 Create", demo::createVectorStoreFileBatch);
        demo.addTitleAction("Demo VectorStoreFileBatch v2 Retrieve", demo::retrieveVectorStoreFileBatch);
        demo.addTitleAction("Demo VectorStoreFileBatch v2 List", demo::listVectorStoreFilesInBatch);
        demo.addTitleAction("Demo VectorStoreFileBatch v2 Cancel", demo::cancelVectorStoreFileBatch);
        demo.addTitleAction("Demo VectorStoreFiles v2 Delete", demo::deletedVectorStoreFiles);
        demo.run();
    }

}
