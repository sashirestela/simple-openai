package io.github.sashirestela.openai.demo;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import io.github.sashirestela.openai.domain.completion.CompletionRequest;
import io.github.sashirestela.openai.domain.completion.CompletionResponse;

public class CompletionServiceDemo extends AbstractDemo {

  private CompletionRequest completionRequest;
  private String modelIdToUse;

  public CompletionServiceDemo() {
    modelIdToUse = "text-davinci-003";
    completionRequest = CompletionRequest.builder()
        .model(modelIdToUse)
        .prompt("Write a technical article about ChatGPT, no more than 100 words.")
        .temperature(0.0)
        .maxTokens(300)
        .build();
  }

  public void demoCallCompletionStreaming() {
    CompletableFuture<Stream<CompletionResponse>> futureCompletion = openAI.completions().createStream(completionRequest);
    Stream<CompletionResponse> completionResponse = futureCompletion.join();
    completionResponse.filter(complResponse -> complResponse.firstText() != null)
        .map(complResponse -> complResponse.firstText())
        .forEach(System.out::print);
    System.out.println();
  }

  public void demoCallCompletionBlocking() {
    CompletableFuture<CompletionResponse> futureCompletion = openAI.completions().create(completionRequest);
    CompletionResponse completionResponse = futureCompletion.join();
    System.out.println(completionResponse.firstText());
  }

  public static void main(String[] args) {
    CompletionServiceDemo demo = new CompletionServiceDemo();

    demo.addTitleAction("Call Completion (Streaming Approach)", () -> demo.demoCallCompletionStreaming());
    demo.addTitleAction("Call Completion (Blocking Approach)", () -> demo.demoCallCompletionBlocking());

    demo.run();
  }
}