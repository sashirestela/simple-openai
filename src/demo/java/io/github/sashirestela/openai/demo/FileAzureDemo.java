package io.github.sashirestela.openai.demo;

public class FileAzureDemo extends FileDemo {

    protected FileAzureDemo() {
        super("azure");
        this.fileProvider = this.openAIAzure;
    }

    public static void main(String[] args) {
        var demo = new FileAzureDemo();

        demo.addTitleAction("Call File Create", demo::demoCallFileCreate);
        demo.addTitleAction("Call File List", demo::demoCallFileGetList);
        demo.addTitleAction("Call File One", demo::demoCallFileGetOne);
        demo.addTitleAction("Call File Delete", demo::demoCallFileDelete);

        demo.run();
    }

}
