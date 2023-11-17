package io.github.sashirestela.openai.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.sashirestela.cleverclient.util.CommonUtil;
import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunctionCall;
import io.github.sashirestela.openai.domain.chat.tool.ChatTool;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolType;

public class FunctionExecutor {
    private Map<String, ChatFunction> mapFunctions = new HashMap<>();

    public FunctionExecutor() {
    }

    public FunctionExecutor(List<ChatFunction> functions) {
        enrollFunctions(functions);
    }

    public List<ChatTool> getToolFunctions() {
        return mapFunctions.values()
            .stream()
            .map(func -> new ChatTool(ChatToolType.FUNCTION, func))
            .collect(Collectors.toList());
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
        if (functionToCall == null || CommonUtil.isNullOrEmpty(functionToCall.getName())) {
            throw new SimpleUncheckedException("No function was entered or it does not has a name.", "", null);
        }
        String functionName = functionToCall.getName();
        if (!mapFunctions.containsKey(functionName)) {
            throw new SimpleUncheckedException("The function {0} was not enrolled in the executor.", functionName,
                    null);
        }
        try {
            var function = mapFunctions.get(functionName);
            var object = JsonUtil.jsonToObjectStrict(functionToCall.getArguments(), function.getFunctionalClass());
            return (T) object.execute();
        } catch (RuntimeException e) {
            throw new SimpleUncheckedException("Cannot execute the function {0}.", functionName, e);
        }
    }
}