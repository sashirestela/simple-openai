package io.github.sashirestela.openai.demo;

import java.util.Arrays;

import io.github.sashirestela.openai.domain.moderation.ModerationRequest;

public class ModerationServiceDemo extends AbstractDemo {

  public void demoCallModerationCreate() {
    var moderationRequest = ModerationRequest.builder()
        .input(Arrays.asList(
            "I want to kill them.",
            "I am not sure what to think about them."))
        .build();
    var futureModeration = openAI.moderations().create(moderationRequest);
    var moderationResponse = futureModeration.join();
    moderationResponse.getResults().stream()
        .forEach(System.out::println);
  }

  public static void main(String[] args) {
    var demo = new ModerationServiceDemo();

    demo.addTitleAction("Call Moderation", demo::demoCallModerationCreate);

    demo.run();
  }
}