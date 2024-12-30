package io.github.sashirestela.openai.demo;

public class EmbeddingMistralDemo extends EmbeddingDemo {

    public EmbeddingMistralDemo(String model) {
        super("mistral", model);
        this.embeddingProvider = this.openAIMistral;
    }

    public static void main(String[] args) {
        var demo = new EmbeddingMistralDemo("mistral-embed");

        demo.addTitleAction("Call Embedding Float Format", demo::demoCallEmbeddingFloat);

        demo.run();
    }

}
