package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.embedding.EmbeddingBase64;
import io.github.sashirestela.openai.domain.embedding.EmbeddingFloat;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;

import java.util.Arrays;

public class EmbeddingDemo extends AbstractDemo {

    public void demoCallEmbeddingFloat() {
        var embeddingRequest = EmbeddingRequest.builder()
                .model("text-embedding-ada-002")
                .input(Arrays.asList(
                        "shiny sun",
                        "blue sky"))
                .build();
        var futureEmbedding = openAI.embeddings().create(embeddingRequest);
        var embeddingResponse = futureEmbedding.join();
        embeddingResponse.getData()
                .stream()
                .map(EmbeddingFloat::getEmbedding)
                .forEach(System.out::println);
    }

    public void demoCallEmbeddingBase64() {
        var embeddingRequest = EmbeddingRequest.builder()
                .model("text-embedding-ada-002")
                .input(Arrays.asList(
                        "shiny sun",
                        "blue sky"))
                .build();
        var futureEmbedding = openAI.embeddings().createBase64(embeddingRequest);
        var embeddingResponse = futureEmbedding.join();
        embeddingResponse.getData()
                .stream()
                .map(EmbeddingBase64::getEmbedding)
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        var demo = new EmbeddingDemo();

        demo.addTitleAction("Call Embedding Float Format", demo::demoCallEmbeddingFloat);
        demo.addTitleAction("Call Embedding Base64 Format", demo::demoCallEmbeddingBase64);

        demo.run();
    }

}
