package io.github.sashirestela.openai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.sashirestela.openai.domain.chat.ChatFunction;
import io.github.sashirestela.openai.domain.chat.ChatFunctionCall;
import io.github.sashirestela.openai.support.JsonUtil;

public class SimpleFunctionExecutor {
  private Map<String, ChatFunction> mapFunctions = new HashMap<>();

  public SimpleFunctionExecutor() {
  }

  public SimpleFunctionExecutor(List<ChatFunction> functions) {
    setFunctions(functions);
  }

  public List<ChatFunction> getFunctions() {
    return new ArrayList<>(mapFunctions.values());
  }

  public void setFunctions(List<ChatFunction> functions) {
    mapFunctions.clear();
    functions.forEach(function -> addFunction(function));
  }

  public void addFunction(ChatFunction function) {
    mapFunctions.put(function.getName(), function);
  }

  @SuppressWarnings("unchecked")
  public <T> T execute(ChatFunctionCall call) {
    try {
      ChatFunction function = mapFunctions.get(call.getName());
      String jsonArguments = call.getArguments();
      Object objArgument = JsonUtil.get().jsonToObject(jsonArguments, function.getParameters());
      T result = (T) function.getFunctionToExecute().apply(objArgument);
      return result;
    } catch (RuntimeException e) {
      throw new SimpleUncheckedException("Cannot execute the function {0}.", call.getName(), e);
    }
  }
}