package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.BaseSimpleOpenAI;
import io.github.sashirestela.openai.SimpleOpenAI;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

public abstract class AbstractDemo {

    private String apiKey;
    private String organizationId;
    protected BaseSimpleOpenAI openAI;

    private static List<TitleAction> titleActions = new ArrayList<>();
    private final int times = 80;

    protected AbstractDemo() {
        apiKey = System.getenv("OPENAI_API_KEY");
        organizationId = System.getenv("OPENAI_ORGANIZATION_ID");
        openAI = SimpleOpenAI.builder()
                .apiKey(apiKey)
                .organizationId(organizationId)
                .build();
    }

    protected AbstractDemo(@NonNull BaseSimpleOpenAI openAI) {
        this.openAI = openAI;
    }

    public void addTitleAction(String title, Action action) {
        titleActions.add(new TitleAction(title, action));
    }

    public void run() {
        titleActions.forEach(ta -> {
            var startTime = System.currentTimeMillis();
            System.out.println("=".repeat(times));
            System.out.println(ta.title);
            System.out.println("-".repeat(times));
            ta.action.execute();
            System.out.println("~".repeat(times / 2));
            var endTime = System.currentTimeMillis();
            var duration = endTime - startTime;
            System.out.println("Duration in milliseconds: " + duration);
            System.out.println();
        });
    }

    @FunctionalInterface
    static interface Action {
        void execute();
    }

    static class TitleAction {
        private String title;
        private Action action;

        public TitleAction(String title, Action action) {
            this.title = title;
            this.action = action;
        }
    }
}