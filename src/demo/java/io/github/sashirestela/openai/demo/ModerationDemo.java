package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.moderation.ModerationRequest;

import java.util.Arrays;

public class ModerationDemo extends AbstractDemo {

    public void demoCallModerationCreate() {
        var moderationRequest = ModerationRequest.builder()
                .input(Arrays.asList(
                        "I want to kill them.",
                        "I am not sure what to think about them."))
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
