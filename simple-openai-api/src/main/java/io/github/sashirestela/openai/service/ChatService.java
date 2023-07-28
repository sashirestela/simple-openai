package io.github.sashirestela.openai.service;

import java.io.IOException;
import java.util.stream.Stream;

import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;

public interface ChatService {
  
  ChatResponse callChatCompletion(ChatRequest chatRequest)
    throws IOException, InterruptedException;

  Stream<ChatResponse> callChatCompletionStream(ChatRequest chatRequest)
    throws IOException, InterruptedException;
  
}