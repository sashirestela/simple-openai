package io.github.sashirestela.openai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.sashirestela.openai.domain.chat.ChatFunction;
import io.github.sashirestela.openai.domain.chat.ChatFunctionCall;
import io.github.sashirestela.openai.support.CommonUtil;
import io.github.sashirestela.openai.support.JsonUtil;

public class SimpleFunctionExecutor {
  private Map<String, ChatFunction> mapFunctions = new HashMap<>();

  public SimpleFunctionExecutor() {
  }

  public SimpleFunctionExecutor(List<ChatFunction> functions) {
    enrollFunctions(functions);
  }

  public List<ChatFunction> getFunctions() {
    return new ArrayList<>(mapFunctions.values());
  }

  public void enrollFunction(ChatFunction function) {
    mapFunctions.put(function.getName(), function);
  }

  public void enrollFunctions(List<ChatFunction> functions) {
    if (functions == null) {
      throw new SimpleUncheckedException("No functions were entered.", "", null);
    }
    mapFunctions.clear();
    functions.forEach(function -> enrollFunction(function));
  }

  @SuppressWarnings("unchecked")
  public <T> T execute(ChatFunctionCall functionToCall) {
    if (functionToCall == null || CommonUtil.get().isNullOrEmpty(functionToCall.getName())) {
      throw new SimpleUncheckedException("No function was entered or it does not has a name.", "", null);
    }
    String functionName = functionToCall.getName();
    if (!mapFunctions.containsKey(functionName)) {
      throw new SimpleUncheckedException("The function {0} was not enrolled in the executor.", functionName, null);
    }
    try {
      ChatFunction function = mapFunctions.get(functionName);
      Object argumentsObj = JsonUtil.get().jsonToObject(functionToCall.getArguments(), function.getParameters());
      T result = (T) function.getFunctionToExecute().apply(argumentsObj);
      return result;
    } catch (RuntimeException e) {
      throw new SimpleUncheckedException("Cannot execute the function {0}.", functionName, e);
    }
  }
}