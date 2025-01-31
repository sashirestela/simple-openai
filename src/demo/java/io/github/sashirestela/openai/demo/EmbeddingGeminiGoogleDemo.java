package io.github.sashirestela.openai.demo;

public class EmbeddingGeminiGoogleDemo extends EmbeddingDemo {

    public EmbeddingGeminiGoogleDemo(String model) {
        super("gemini_google", model);
        this.embeddingProvider = openAIGeminiGoogle;
    }

    public static void main(String[] args) {
        var demo = new EmbeddingGeminiGoogleDemo("text-embedding-004");

        demo.addTitleAction("Call Embedding Float Format", demo::demoCallEmbeddingFloat);

        demo.run();
    }

}
