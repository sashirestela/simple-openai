package io.github.sashirestela.openai;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import io.github.sashirestela.openai.domain.audio.AudioResponse;
import io.github.sashirestela.openai.domain.audio.AudioTranscribeRequest;
import io.github.sashirestela.openai.domain.audio.AudioTranslateRequest;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.model.ModelResponse;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.Multipart;
import io.github.sashirestela.openai.http.annotation.POST;
import io.github.sashirestela.openai.http.annotation.Path;

interface OpenAI {

  interface Models {

    @GET("/v1/models")
    CompletableFuture<List<ModelResponse>> getList();

    @GET("/v1/models/{modelId}")
    CompletableFuture<ModelResponse> getOne(@Path("modelId") String modelId);

  }

  interface ChatCompletions {

    @POST("/v1/chat/completions")
    CompletableFuture<ChatResponse> create(@Body ChatRequest chatRequest);

    @POST("/v1/chat/completions")
    CompletableFuture<Stream<ChatResponse>> createStream(@Body ChatRequest chatRequest);

  }

  interface Audios {

    @Multipart
    @POST("/v1/audio/transcriptions")
    CompletableFuture<AudioResponse> transcribe(@Body AudioTranscribeRequest audioRequest);

    @Multipart
    @POST("/v1/audio/translations")
    CompletableFuture<AudioResponse> translate(@Body AudioTranslateRequest audioRequest);

  }

}
