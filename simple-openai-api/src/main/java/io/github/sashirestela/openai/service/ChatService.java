package io.github.sashirestela.openai.service;

import java.io.IOException;
import java.util.stream.Stream;

import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.POST;
import io.github.sashirestela.openai.http.annotation.Streaming;

public interface ChatService {
  
  @POST("/v1/chat/completions")
  ChatResponse callChatCompletion(@Body ChatRequest chatRequest) throws IOException, InterruptedException;

  @Streaming
  @POST("/v1/chat/completions")
  Stream<ChatResponse> callChatCompletionStream(@Body ChatRequest chatRequest) throws IOException, InterruptedException;
  
}