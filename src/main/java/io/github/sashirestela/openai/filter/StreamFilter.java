package io.github.sashirestela.openai.filter;

import java.lang.reflect.Method;

import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.completion.CompletionRequest;
import io.github.sashirestela.openai.http.InvocationFilter;
import io.github.sashirestela.openai.http.ReturnType;

public class StreamFilter implements InvocationFilter {

  @Override
  public void apply(Method method, Object[] arguments) {
    boolean isStreaming = new ReturnType(method).isStream();
    if (arguments[0] instanceof ChatRequest) {
      ChatRequest request = (ChatRequest) arguments[0];
      request.setStream(isStreaming);
    } else {
      CompletionRequest request = (CompletionRequest) arguments[0];
      request.setStream(isStreaming);
    }
  }
}
