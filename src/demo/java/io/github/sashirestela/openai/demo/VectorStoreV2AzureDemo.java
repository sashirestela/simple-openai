package io.github.sashirestela.openai.demo;

public class VectorStoreV2AzureDemo extends VectorStoreV2Demo {

    protected VectorStoreV2AzureDemo() {
        super("azure", new FileAzureDemo());
        this.assistantProvider = this.openAIAzure;
    }

    public static void main(String[] args) {
        var demo = new VectorStoreV2AzureDemo();
        demo.prepareDemo();
        demo.addTitleAction("Demo VectorStore v2 Create", demo::createVectorStore);
        demo.addTitleAction("Demo VectorStore v2 Modify", demo::modifyVectorStore);
        demo.addTitleAction("Demo VectorStore v2 Retrieve", demo::retrieveVectorStore);
        demo.addTitleAction("Demo VectorStore v2 List", demo::listVectorStores);
        demo.addTitleAction("Demo VectorStore v2 Delete", demo::deleteVectorStore);
        demo.run();
    }

}
