package io.github.sashirestela.openai.demo;

public class VectorStoreFileV2AzureDemo extends VectorStoreFileV2Demo {

    protected VectorStoreFileV2AzureDemo() {
        super("azure", new FileAzureDemo());
        this.assistantProvider = this.openAIAzure;
    }

    public static void main(String[] args) {
        var demo = new VectorStoreFileV2AzureDemo();
        demo.prepareDemo();
        demo.addTitleAction("Demo VectorStoreFile v2 Create", demo::createVectorStoreFile);
        demo.addTitleAction("Demo VectorStoreFile v2 Retrieve", demo::retrieveVectorStoreFile);
        demo.addTitleAction("Demo VectorStoreFile v2 List", demo::listVectorStoreFiles);
        demo.addTitleAction("Demo VectorStoreFile v2 Delete", demo::deletedVectorStoreFile);
        demo.run();
    }

}
