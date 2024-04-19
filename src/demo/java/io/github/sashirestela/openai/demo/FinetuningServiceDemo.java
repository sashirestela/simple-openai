package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.finetuning.FineTuningRequest;

public class FinetuningServiceDemo extends AbstractDemo {

    private FileServiceDemo fileServiceDemo;
    private String fileId;
    private String fineTuningId;

    public FinetuningServiceDemo() {
        fileServiceDemo = new FileServiceDemo();
    }

    public void demoCallFineTuningCreate() {
        System.out.println("This functionality requires that a file has been uploaded with the Files service");
        System.out.println("and you has to wait until its status is 'processed', which could take some time.");
        var console = System.console();
        fileId = console.readLine("\nEnter the fileId of a file in status 'processed':");
        fileServiceDemo.waitUntilFileIsProcessed(fileId);
        var fineTuningRequest = FineTuningRequest.builder()
                .trainingFile(fileId)
                .model("gpt-3.5-turbo-1106")
                .build();
        var futureFineTuning = openAI.fineTunings().create(fineTuningRequest);
        var fineTuningResponse = futureFineTuning.join();
        fineTuningId = fineTuningResponse.getId();
        System.out.println(fineTuningResponse);
    }

    public void demoCallFineTuningGetList() {
        var futureFineTuning = openAI.fineTunings().getList(null, null);
        var fineTuningResponses = futureFineTuning.join();
        fineTuningResponses.stream()
                .forEach(System.out::println);
    }

    public void demoCallFineTuningGetOne() {
        var futureFineTuning = openAI.fineTunings().getOne(fineTuningId);
        var fineTuningResponse = futureFineTuning.join();
        System.out.println(fineTuningResponse);
    }

    public void demoCallFineTuningGetEvents() {
        var futureEvents = openAI.fineTunings().getEvents(fineTuningId, 2, null);
        var fineTuningEvents = futureEvents.join();
        fineTuningEvents.stream()
                .forEach(System.out::println);
    }

    public void demoCallFineTuningCancel() {
        var futureFineTuning = openAI.fineTunings().cancel(fineTuningId);
        var fineTuningResponse = futureFineTuning.join();
        System.out.println(fineTuningResponse);
        fileServiceDemo.deleteFile(fileId);
    }

    public static void main(String[] args) {
        var demo = new FinetuningServiceDemo();

        demo.addTitleAction("Call FineTuning Create", demo::demoCallFineTuningCreate);
        demo.addTitleAction("Call FineTuning List", demo::demoCallFineTuningGetList);
        demo.addTitleAction("Call FineTuning One", demo::demoCallFineTuningGetOne);
        demo.addTitleAction("Call FineTuning Events", demo::demoCallFineTuningGetEvents);
        demo.addTitleAction("Call FineTuning Cancel", demo::demoCallFineTuningCancel);

        demo.run();
    }

}
