package io.github.sashirestela.openai.demo;

public class VectorStoreFileBatchV2AzureDemo extends VectorStoreFileBatchV2Demo {

    protected VectorStoreFileBatchV2AzureDemo() {
        super("azure", new FileAzureDemo());
        this.assistantProvider = this.openAIAzure;
    }

    public static void main(String[] args) {
        var demo = new VectorStoreFileBatchV2AzureDemo();
        demo.prepareDemo();
        demo.addTitleAction("Demo VectorStoreFileBatch v2 Create", demo::createVectorStoreFileBatch);
        demo.addTitleAction("Demo VectorStoreFileBatch v2 Retrieve", demo::retrieveVectorStoreFileBatch);
        demo.addTitleAction("Demo VectorStoreFileBatch v2 List", demo::listVectorStoreFilesInBatch);
        demo.addTitleAction("Demo VectorStoreFiles v2 Delete", demo::deletedVectorStoreFiles);
        demo.run();
    }

}
