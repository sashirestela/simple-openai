package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.embedding.EmbeddingBase64;
import io.github.sashirestela.openai.domain.embedding.EmbeddingFloat;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;
import io.github.sashirestela.openai.service.EmbeddingServices;

import java.util.Arrays;

public class EmbeddingDemo extends AbstractDemo {

    protected String model;
    protected EmbeddingServices embeddingProvider;

    public EmbeddingDemo(String model) {
        this("standard", model);
    }

    protected EmbeddingDemo(String provider, String model) {
        super(provider);
        this.model = model;
        this.embeddingProvider = this.openAI;
    }

    public void demoCallEmbeddingFloat() {
        var embeddingRequest = EmbeddingRequest.builder()
                .model(this.model)
                .input(Arrays.asList(
                        "shiny sun",
                        "blue sky"))
                .build();
        var futureEmbedding = embeddingProvider.embeddings().create(embeddingRequest);
        var embeddingResponse = futureEmbedding.join();
        embeddingResponse.getData()
                .stream()
                .map(EmbeddingFloat::getEmbedding)
                .forEach(System.out::println);
    }

    public void demoCallEmbeddingBase64() {
        var embeddingRequest = EmbeddingRequest.builder()
                .model(this.model)
                .input(Arrays.asList(
                        "shiny sun",
                        "blue sky"))
                .build();
        var futureEmbedding = embeddingProvider.embeddings().createBase64(embeddingRequest);
        var embeddingResponse = futureEmbedding.join();
        embeddingResponse.getData()
                .stream()
                .map(EmbeddingBase64::getEmbedding)
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        var demo = new EmbeddingDemo("text-embedding-3-small");

        demo.addTitleAction("Call Embedding Float Format", demo::demoCallEmbeddingFloat);
        demo.addTitleAction("Call Embedding Base64 Format", demo::demoCallEmbeddingBase64);

        demo.run();
    }

}
