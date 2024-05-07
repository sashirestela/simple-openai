package io.github.sashirestela.openai.common.function;

import io.github.sashirestela.cleverclient.util.CommonUtil;
import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.common.tool.Tool;
import io.github.sashirestela.openai.common.tool.ToolCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FunctionExecutor {

    private Map<String, FunctionDef> mapFunctions = new HashMap<>();

    public FunctionExecutor() {
    }

    public FunctionExecutor(List<FunctionDef> functions) {
        enrollFunctions(functions);
    }

    public List<Tool> getToolFunctions() {
        return mapFunctions.values()
                .stream()
                .map(func -> Tool.function(func))
                .collect(Collectors.toList());
    }

    public void enrollFunction(FunctionDef function) {
        mapFunctions.put(function.getName(), function);
    }

    public void enrollFunctions(List<FunctionDef> functions) {
        if (functions == null) {
            throw new SimpleUncheckedException("No functions were entered.", "", null);
        }
        mapFunctions.clear();
        functions.forEach(function -> enrollFunction(function));
    }

    @SuppressWarnings("unchecked")
    public <T> T execute(FunctionCall functionCall) {
        if (functionCall == null || CommonUtil.isNullOrEmpty(functionCall.getName())) {
            throw new SimpleUncheckedException("No function was entered or it does not has a name.", "", null);
        }
        String functionName = functionCall.getName();
        if (!mapFunctions.containsKey(functionName)) {
            throw new SimpleUncheckedException("The function {0} was not enrolled in the executor.", functionName,
                    null);
        }
        try {
            var function = mapFunctions.get(functionName);
            var object = JsonUtil.jsonToObject(functionCall.getArguments(), function.getFunctionalClass());
            return (T) object.execute();
        } catch (RuntimeException e) {
            throw new SimpleUncheckedException("Cannot execute the function {0}.", functionName, e);
        }
    }

    public <R> List<R> executeAll(List<ToolCall> toolCalls, BiFunction<String, String, R> toolOutputItem) {
        List<R> toolOutputs = new ArrayList<>();
        for (var toolCall : toolCalls) {
            if (toolCall.getFunction() != null) {
                var result = execute(toolCall.getFunction());
                var item = toolOutputItem.apply(toolCall.getId(), result.toString());
                toolOutputs.add(item);
            }
        }
        return toolOutputs;
    }

}
