package io.github.sashirestela.openai.demo;

import java.io.Console;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.finetuning.FineTuningEvent;
import io.github.sashirestela.openai.domain.finetuning.FineTuningRequest;
import io.github.sashirestela.openai.domain.finetuning.FineTuningResponse;

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
    Console console = System.console();
    fileId = console.readLine("\nEnter the fileId of a file in status 'processed':");
    fileServiceDemo.waitUntilFileIsProcessed(fileId);
    FineTuningRequest fineTuningRequest = FineTuningRequest.builder()
        .trainingFile(fileId)
        .model("gpt-3.5-turbo-0613")
        .build();
    CompletableFuture<FineTuningResponse> futureFineTuning = openAI.fineTunings().create(fineTuningRequest);
    FineTuningResponse fineTuningResponse = futureFineTuning.join();
    fineTuningId = fineTuningResponse.getId();
    System.out.println(fineTuningResponse);
  }

  public void demoCallFineTuningGetList() {
    CompletableFuture<List<FineTuningResponse>> futureFineTuning = openAI.fineTunings().getList(null, null);
    List<FineTuningResponse> fineTuningResponses = futureFineTuning.join();
    fineTuningResponses.stream()
        .forEach(System.out::println);
  }

  public void demoCallFineTuningGetOne() {
    CompletableFuture<FineTuningResponse> futureFineTuning = openAI.fineTunings().getOne(fineTuningId);
    FineTuningResponse fineTuningResponse = futureFineTuning.join();
    System.out.println(fineTuningResponse);
  }

  public void demoCallFineTuningGetEvents() {
    CompletableFuture<List<FineTuningEvent>> futureEvents = openAI.fineTunings().getEvents(fineTuningId, 2, null);
    List<FineTuningEvent> fineTuningEvents = futureEvents.join();
    fineTuningEvents.stream()
        .forEach(System.out::println);
  }

  public void demoCallFineTuningCancel() {
    CompletableFuture<FineTuningResponse> futureFineTuning = openAI.fineTunings().cancel(fineTuningId);
    FineTuningResponse fineTuningResponse = futureFineTuning.join();
    System.out.println(fineTuningResponse);
    fileServiceDemo.deleteFileResponse(fileId);
  }

  public static void main(String[] args) {
    FinetuningServiceDemo demo = new FinetuningServiceDemo();

    demo.addTitleAction("Call FineTuning Create", () -> demo.demoCallFineTuningCreate());
    demo.addTitleAction("Call FineTuning List", () -> demo.demoCallFineTuningGetList());
    demo.addTitleAction("Call FineTuning One", () -> demo.demoCallFineTuningGetOne());
    demo.addTitleAction("Call FineTuning Events", () -> demo.demoCallFineTuningGetEvents());
    demo.addTitleAction("Call FineTuning Cancel", () -> demo.demoCallFineTuningCancel());

    demo.run();
  }
}