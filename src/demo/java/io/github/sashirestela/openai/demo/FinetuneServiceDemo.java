package io.github.sashirestela.openai.demo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.finetune.FineTuneEvent;
import io.github.sashirestela.openai.domain.finetune.FineTuneRequest;
import io.github.sashirestela.openai.domain.finetune.FineTuneResponse;

public class FinetuneServiceDemo extends AbstractDemo {

  private FileServiceDemo fileServiceDemo;
  private String fileId;
  private String fineTuneId;

  public FinetuneServiceDemo() {
    fileServiceDemo = new FileServiceDemo();
    fileId = fileServiceDemo.createFileResponse().getId();
  }

  public void demoCallFineTuneCreate() {
    FineTuneRequest fineTuneRequest = FineTuneRequest.builder()
        .trainingFile(fileId)
        .build();
    CompletableFuture<FineTuneResponse> futureFineTune = openAI.fineTuneService().create(fineTuneRequest);
    FineTuneResponse fineTuneResponse = futureFineTune.join();
    fineTuneId = fineTuneResponse.getId();
    System.out.println(fineTuneResponse);
  }

  public void demoCallFineTuneGetList() {
    CompletableFuture<List<FineTuneResponse>> futureFineTune = openAI.fineTuneService().getList();
    List<FineTuneResponse> fineTuneResponses = futureFineTune.join();
    fineTuneResponses.stream()
        .forEach(System.out::println);
  }

  public void demoCallFineTuneGetOne() {
    CompletableFuture<FineTuneResponse> futureFineTune = openAI.fineTuneService().getOne(fineTuneId);
    FineTuneResponse fineTuneResponse = futureFineTune.join();
    System.out.println(fineTuneResponse);
  }

  public void demoCallFineTuneGetEvents() {
    CompletableFuture<List<FineTuneEvent>> futureEvents = openAI.fineTuneService().getEvents(fineTuneId);
    List<FineTuneEvent> fineTuneEvents = futureEvents.join();
    fineTuneEvents.stream()
        .forEach(System.out::println);
  }

  public void demoCallFineTuneCancel() {
    CompletableFuture<FineTuneResponse> futureFineTune = openAI.fineTuneService().cancel(fineTuneId);
    FineTuneResponse fineTuneResponse = futureFineTune.join();
    System.out.println(fineTuneResponse);
    fileServiceDemo.deleteFileResponse(fileId);
  }

  public static void main(String[] args) {
    FinetuneServiceDemo demo = new FinetuneServiceDemo();

    demo.addTitleAction("Call FineTune Create", () -> demo.demoCallFineTuneCreate());
    demo.addTitleAction("Call FineTune List", () -> demo.demoCallFineTuneGetList());
    demo.addTitleAction("Call FineTune One", () -> demo.demoCallFineTuneGetOne());
    demo.addTitleAction("Call FineTune Events", () -> demo.demoCallFineTuneGetEvents());
    demo.addTitleAction("Call FineTune Cancel", () -> demo.demoCallFineTuneCancel());

    demo.run();
  }
}