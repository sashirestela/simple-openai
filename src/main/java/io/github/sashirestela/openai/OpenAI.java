package io.github.sashirestela.openai;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import io.github.sashirestela.openai.domain.audio.AudioResponse;
import io.github.sashirestela.openai.domain.audio.AudioTranscribeRequest;
import io.github.sashirestela.openai.domain.audio.AudioTranslateRequest;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.completion.CompletionRequest;
import io.github.sashirestela.openai.domain.completion.CompletionResponse;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;
import io.github.sashirestela.openai.domain.embedding.EmbeddingResponse;
import io.github.sashirestela.openai.domain.file.FileDeletedResponse;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileResponse;
import io.github.sashirestela.openai.domain.finetune.FineTuneEvent;
import io.github.sashirestela.openai.domain.finetune.FineTuneRequest;
import io.github.sashirestela.openai.domain.finetune.FineTuneResponse;
import io.github.sashirestela.openai.domain.image.ImageEditsRequest;
import io.github.sashirestela.openai.domain.image.ImageRequest;
import io.github.sashirestela.openai.domain.image.ImageResponse;
import io.github.sashirestela.openai.domain.image.ImageVariationsRequest;
import io.github.sashirestela.openai.domain.model.ModelResponse;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.DELETE;
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

  interface Completions {

    @POST("/v1/completions")
    CompletableFuture<CompletionResponse> create(@Body CompletionRequest completionRequest);

    @POST("/v1/completions")
    CompletableFuture<Stream<CompletionResponse>> createStream(@Body CompletionRequest completionRequest);

  }

  interface Images {

    @POST("/v1/images/generations")
    CompletableFuture<List<ImageResponse>> create(@Body ImageRequest imageRequest);

    @Multipart
    @POST("/v1/images/edits")
    CompletableFuture<List<ImageResponse>> createEdits(@Body ImageEditsRequest imageRequest);

    @Multipart
    @POST("/v1/images/variations")
    CompletableFuture<List<ImageResponse>> createVariations(@Body ImageVariationsRequest imageRequest);

  }

  interface Embeddings {

    @POST("/v1/embeddings")
    CompletableFuture<EmbeddingResponse> create(@Body EmbeddingRequest embeddingRequest);

  }

  interface Audios {

    @Multipart
    @POST("/v1/audio/transcriptions")
    CompletableFuture<AudioResponse> transcribe(@Body AudioTranscribeRequest audioRequest);

    @Multipart
    @POST("/v1/audio/translations")
    CompletableFuture<AudioResponse> translate(@Body AudioTranslateRequest audioRequest);

  }

  interface Files {

    @Multipart
    @POST("/v1/files")
    CompletableFuture<FileResponse> create(@Body FileRequest fileRequest);

    @GET("/v1/files")
    CompletableFuture<List<FileResponse>> getList();

    @GET("/v1/files/{fileId}")
    CompletableFuture<FileResponse> getOne(@Path("fileId") String fileId);

    // TO-DO: handle response content type or InputStream as response type.
    @GET("/v1/files/{fileId}/content")
    CompletableFuture<String> getContent(@Path("fileId") String fileId);

    @DELETE("/v1/files/{fileId}")
    CompletableFuture<FileDeletedResponse> delete(@Path("fileId") String fileId);

  }

  interface FineTunes {

    @POST("/v1/fine-tunes")
    CompletableFuture<FineTuneResponse> create(@Body FineTuneRequest fineTuneRequest);

    @GET("/v1/fine-tunes")
    CompletableFuture<List<FineTuneResponse>> getList();

    @GET("/v1/fine-tunes/{fineTuneId}")
    CompletableFuture<FineTuneResponse> getOne(@Path("fineTuneId") String fineTuneId);

    @GET("/v1/fine-tunes/{fineTuneId}/events")
    CompletableFuture<List<FineTuneEvent>> getEvents(@Path("fineTuneId") String fineTuneId);

    @POST("/v1/fine-tunes/{fineTuneId}/cancel")
    CompletableFuture<FineTuneResponse> cancel(@Path("fineTuneId") String fineTuneId);

  }

}