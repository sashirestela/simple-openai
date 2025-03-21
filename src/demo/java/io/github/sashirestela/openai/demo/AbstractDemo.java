package io.github.sashirestela.openai.demo;

import io.github.sashirestela.cleverclient.client.OkHttpClientAdapter;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.SimpleOpenAIAnyscale;
import io.github.sashirestela.openai.SimpleOpenAIAzure;
import io.github.sashirestela.openai.SimpleOpenAIDeepseek;
import io.github.sashirestela.openai.SimpleOpenAIGeminiGoogle;
import io.github.sashirestela.openai.SimpleOpenAIGeminiVertex;
import io.github.sashirestela.openai.SimpleOpenAIMistral;
import io.github.sashirestela.openai.base.OpenAIProvider;
import lombok.NonNull;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractDemo {

    private OpenAIProvider currentOpenAI;
    protected SimpleOpenAI openAI;
    protected SimpleOpenAIAzure openAIAzure;
    protected SimpleOpenAIAnyscale openAIAnyscale;
    protected SimpleOpenAIMistral openAIMistral;
    protected SimpleOpenAIDeepseek openAIDeepseek;
    protected SimpleOpenAIGeminiGoogle openAIGeminiGoogle;
    protected SimpleOpenAIGeminiVertex openAIGeminiVertex;

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
                currentOpenAI = openAI;
                break;
            case "azure":
                openAIAzure = SimpleOpenAIAzure.builder()
                        .apiKey(System.getenv("AZURE_OPENAI_API_KEY"))
                        .apiVersion(System.getenv("AZURE_OPENAI_API_VERSION"))
                        .baseUrl(System.getenv("AZURE_OPENAI_BASE_URL"))
                        .build();

                currentOpenAI = openAIAzure;
                break;
            case "anyscale":
                openAIAnyscale = SimpleOpenAIAnyscale.builder()
                        .apiKey(System.getenv("ANYSCALE_API_KEY"))
                        .clientAdapter(new OkHttpClientAdapter(new OkHttpClient(
                                new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS))))
                        .build();
                currentOpenAI = openAIAnyscale;
                break;
            case "mistral":
                openAIMistral = SimpleOpenAIMistral.builder()
                        .apiKey(System.getenv("MISTRAL_API_KEY"))
                        .build();
                currentOpenAI = openAIMistral;
                break;
            case "deepseek":
                openAIDeepseek = SimpleOpenAIDeepseek.builder()
                        .apiKey(System.getenv("DEEPSEEK_API_KEY"))
                        .build();
                currentOpenAI = openAIDeepseek;
                break;
            case "gemini_google":
                openAIGeminiGoogle = SimpleOpenAIGeminiGoogle.builder()
                        .apiKey(System.getenv("GEMINIGOOGLE_API_KEY"))
                        .build();
                currentOpenAI = openAIGeminiGoogle;
                break;
            case "gemini_vertex":
                openAIGeminiVertex = SimpleOpenAIGeminiVertex.builder()
                        .baseUrl(System.getenv("GEMINI_VERTEX_BASE_URL"))
                        .apiKeyProvider(ChatGeminiVertexDemo::getApiKey)
                        .build();
                currentOpenAI = openAIGeminiVertex;
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
        currentOpenAI.shutDown();
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
