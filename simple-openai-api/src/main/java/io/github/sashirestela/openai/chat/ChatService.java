package io.github.sashirestela.openai.chat;

import java.io.IOException;
import java.util.stream.Stream;

public interface ChatService {
  
  ChatResponse callChatCompletion(ChatRequest chatRequest)
    throws IOException, InterruptedException;

  Stream<ChatResponse> callChatCompletionStream(ChatRequest chatRequest)
    throws IOException, InterruptedException;
  
}