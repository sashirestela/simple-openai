package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.completion.Completion;
import io.github.sashirestela.openai.domain.completion.CompletionRequest;

public class CompletionDemo extends AbstractDemo {

    private CompletionRequest completionRequest;
    private String modelIdToUse;

    public CompletionDemo() {
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
        completionResponse.forEach(CompletionDemo::processResponseChunk);
        System.out.println();
    }

    private static void processResponseChunk(Completion responseChunk) {
        var choices = responseChunk.getChoices();
        if (!choices.isEmpty()) {
            var delta = choices.get(0).getText();
            System.out.print(delta);
        }
        var usage = responseChunk.getUsage();
        if (usage != null) {
            System.out.println("\n");
            System.out.println(usage);
        }
    }

    public void demoCallCompletionBlocking() {
        var futureCompletion = openAI.completions().create(completionRequest);
        var completionResponse = futureCompletion.join();
        System.out.println(completionResponse.firstText());
    }

    public static void main(String[] args) {
        var demo = new CompletionDemo();

        demo.addTitleAction("Call Completion (Streaming Approach)", demo::demoCallCompletionStreaming);
        demo.addTitleAction("Call Completion (Blocking Approach)", demo::demoCallCompletionBlocking);

        demo.run();
    }

}
