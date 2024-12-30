package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.SimpleOpenAIAnyscale;
import io.github.sashirestela.openai.SimpleOpenAIAzure;
import io.github.sashirestela.openai.SimpleOpenAIMistral;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDemo {

    protected SimpleOpenAI openAI;
    protected SimpleOpenAIAzure openAIAzure;
    protected SimpleOpenAIAnyscale openAIAnyscale;
    protected SimpleOpenAIMistral openAIMistral;

    private static List<TitleAction> titleActions = new ArrayList<>();
    private static final int TIMES = 80;

    protected AbstractDemo() {
        this("standard");
    }

    protected AbstractDemo(String provider) {
        switch (provider.toLowerCase()) {
            case "standard":
                openAI = SimpleOpenAI.builder()
                        .apiKey(System.getenv("OPENAI_API_KEY"))
                        .organizationId(System.getenv("OPENAI_ORGANIZATION_ID"))
                        .build();
                break;
            case "azure":
                openAIAzure = SimpleOpenAIAzure.builder()
                        .apiKey(System.getenv("AZURE_OPENAI_API_KEY"))
                        .apiVersion(System.getenv("AZURE_OPENAI_API_VERSION"))
                        .baseUrl(System.getenv("AZURE_OPENAI_BASE_URL"))
                        .build();
                break;
            case "anyscale":
                openAIAnyscale = SimpleOpenAIAnyscale.builder()
                        .apiKey(System.getenv("ANYSCALE_API_KEY"))
                        .build();
                break;
            case "mistral":
                openAIMistral = SimpleOpenAIMistral.builder()
                        .apiKey(System.getenv("MISTRAL_API_KEY"))
                        .build();
                break;
            default:
                break;
        }
    }

    protected AbstractDemo(@NonNull SimpleOpenAI openAI) {
        this.openAI = openAI;
    }

    public void addTitleAction(String title, Action action) {
        titleActions.add(new TitleAction(title, action));
    }

    public void run() {
        titleActions.forEach(ta -> {
            var startTime = System.currentTimeMillis();
            System.out.println("=".repeat(TIMES));
            System.out.println(ta.title);
            System.out.println("-".repeat(TIMES));
            ta.action.execute();
            System.out.println("~".repeat(TIMES / 2));
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
