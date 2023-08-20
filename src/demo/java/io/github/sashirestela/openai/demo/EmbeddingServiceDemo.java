package io.github.sashirestela.openai.demo;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;
import io.github.sashirestela.openai.domain.embedding.EmbeddingResponse;

public class EmbeddingServiceDemo extends AbstractDemo {

  public EmbeddingServiceDemo() {
  }

  public void demoCallEmbedding() {
    EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
        .model("text-embedding-ada-002")
        .input(Arrays.asList(
            "Sample phrase",
            "to test embedding."))
        .build();
    CompletableFuture<EmbeddingResponse> futureEmbedding = openAI.embeddings().create(embeddingRequest);
    EmbeddingResponse embeddingResponse = futureEmbedding.join();
    embeddingResponse.getData().stream()
      .map(embeddingObject -> embeddingObject.getEmbedding())
      .forEach(System.out::println);
  }

  public static void main(String[] args) {
    EmbeddingServiceDemo demo = new EmbeddingServiceDemo();

    demo.addTitleAction("Call Embedding", () -> demo.demoCallEmbedding());

    demo.run();
  }
}
