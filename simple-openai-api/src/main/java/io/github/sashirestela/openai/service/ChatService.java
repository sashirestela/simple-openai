package io.github.sashirestela.openai.service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.POST;
import io.github.sashirestela.openai.http.annotation.Streaming;

public interface ChatService {

  @POST("/v1/chat/completions")
  CompletableFuture<ChatResponse> callChatCompletion(@Body ChatRequest chatRequest);

  @Streaming
  @POST("/v1/chat/completions")
  CompletableFuture<Stream<ChatResponse>> callChatCompletionStream(@Body ChatRequest chatRequest);

}