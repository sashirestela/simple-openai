package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.moderation.ModerationRequest;
import io.github.sashirestela.openai.domain.moderation.ModerationRequest.MultiModalInput.ImageUrlInput;
import io.github.sashirestela.openai.domain.moderation.ModerationRequest.MultiModalInput.TextInput;

import java.util.Arrays;

public class ModerationDemo extends AbstractDemo {

    public void demoCallModerationCreate() {
        var moderationRequest = ModerationRequest.builder()
                .input(Arrays.asList(
                        TextInput.of("I want to kill them."),
                        ImageUrlInput.of("https://upload.wikimedia.org/wikipedia/commons/e/e3/BWHammerSickle.jpg"),
                        TextInput.of("I am not sure what to think about them.")))
                .model("omni-moderation-latest")
                .build();
        var futureModeration = openAI.moderations().create(moderationRequest);
        var moderationResponse = futureModeration.join();
        moderationResponse.getResults()
                .stream()
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        var demo = new ModerationDemo();

        demo.addTitleAction("Call Moderation", demo::demoCallModerationCreate);

        demo.run();
    }

}
