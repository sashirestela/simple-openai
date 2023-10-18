package io.github.sashirestela.openai.demo;

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
    var futureCompletion = openAI.completions().createStream(completionRequest);
    var completionResponse = futureCompletion.join();
    completionResponse.filter(complResponse -> complResponse.firstText() != null)
        .map(CompletionResponse::firstText)
        .forEach(System.out::print);
    System.out.println();
  }

  public void demoCallCompletionBlocking() {
    var futureCompletion = openAI.completions().create(completionRequest);
    var completionResponse = futureCompletion.join();
    System.out.println(completionResponse.firstText());
  }

  public static void main(String[] args) {
    var demo = new CompletionServiceDemo();

    demo.addTitleAction("Call Completion (Streaming Approach)", demo::demoCallCompletionStreaming);
    demo.addTitleAction("Call Completion (Blocking Approach)", demo::demoCallCompletionBlocking);

    demo.run();
  }
}