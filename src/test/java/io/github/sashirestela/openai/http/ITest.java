package io.github.sashirestela.openai.http;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.POST;
import io.github.sashirestela.openai.http.annotation.Path;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

interface ITest {

  interface NotAnnotatedService {

    @GET("/demos")
    CompletableFuture<List<Demo>> goodMethod();

    String unannotatedMethod();

  }

  interface BadPathParamService {

    @GET("/demos")
    CompletableFuture<List<Demo>> goodMethod();

    @POST("/demos/{demoId}")
    CompletableFuture<Demo> unmatchedPathParamMethod(@Path("demo_id") int demoId);

  }

  interface GoodService {

    @GET("/demos")
    List<Demo> unsupportedMethod();

    @GET("/demos/{demoId}")
    CompletableFuture<String> getDemoPlain(@Path("demoId") Integer demoId);

    @GET("/demos/{demoId}")
    CompletableFuture<Demo> getDemo(@Path("demoId") Integer demoId);

    @GET("/demos")
    CompletableFuture<List<Demo>> getDemos();

    @POST("/demos")
    CompletableFuture<Stream<Demo>> getDemoStream(@Body RequestDemo request);

  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @ToString
  @EqualsAndHashCode
  static class Demo {

    private Integer id;

    private String description;

    private Boolean active;

  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  static class RequestDemo {

    private String prefix;

  }
}