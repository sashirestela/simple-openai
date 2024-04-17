package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.completion.CompletionRequest;
import io.github.sashirestela.openai.domain.completion.CompletionResponse;

public class CompletionServiceDemo extends AbstractDemo {

    private CompletionRequest completionRequest;
    private String modelIdToUse;

    public CompletionServiceDemo() {
        modelIdToUse = "gpt-3.5-turbo-instruct";
        completionRequest = CompletionRequest.builder()
                .model(modelIdToUse)
                .prompt("Tell me the Pythagorean theorem in no more than 50 words.")
                .temperature(0.0)
                .maxTokens(300)
                .seed(1)
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
