package io.github.sashirestela.openai.demo;

import java.util.Arrays;

import io.github.sashirestela.openai.domain.embedding.Embedding;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;

public class EmbeddingServiceDemo extends AbstractDemo {

  public void demoCallEmbedding() {
    var embeddingRequest = EmbeddingRequest.builder()
        .model("text-embedding-ada-002")
        .input(Arrays.asList(
            "Sample phrase",
            "to test embedding."))
        .build();
    var futureEmbedding = openAI.embeddings().create(embeddingRequest);
    var embeddingResponse = futureEmbedding.join();
    embeddingResponse.getData().stream()
      .map(Embedding::getEmbedding)
      .forEach(System.out::println);
  }

  public static void main(String[] args) {
    var demo = new EmbeddingServiceDemo();

    demo.addTitleAction("Call Embedding", demo::demoCallEmbedding);

    demo.run();
  }
}