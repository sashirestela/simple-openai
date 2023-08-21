package io.github.sashirestela.openai.demo;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.moderation.ModerationRequest;
import io.github.sashirestela.openai.domain.moderation.ModerationResponse;

public class ModerationServiceDemo extends AbstractDemo {

  public ModerationServiceDemo() {
  }

  public void demoCallModerationCreate() {
    ModerationRequest moderationRequest = ModerationRequest.builder()
        .input(Arrays.asList(
            "I want to kill them.",
            "I am not sure what to think about them."))
        .build();
    CompletableFuture<ModerationResponse> futureModeration = openAI.moderations().create(moderationRequest);
    ModerationResponse moderationResponse = futureModeration.join();
    moderationResponse.getResults().stream()
        .forEach(System.out::println);
  }

  public static void main(String[] args) {
    ModerationServiceDemo demo = new ModerationServiceDemo();

    demo.addTitleAction("Call Moderation", () -> demo.demoCallModerationCreate());

    demo.run();
  }
}