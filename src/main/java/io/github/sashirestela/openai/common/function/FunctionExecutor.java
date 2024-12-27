package io.github.sashirestela.openai.common.function;

import io.github.sashirestela.cleverclient.util.CommonUtil;
import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.openai.common.tool.Tool;
import io.github.sashirestela.openai.common.tool.ToolCall;
import io.github.sashirestela.openai.common.tool.ToolChoice;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import io.github.sashirestela.openai.exception.SimpleOpenAIException;

import java.util.ArrayList;
import java.util.Arrays;
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
                .map(Tool::function)
                .collect(Collectors.toList());
    }

    public List<Tool> getToolFunctions(Object toolChoice) {
        if (toolChoice instanceof ToolChoiceOption) {
            var choice = (ToolChoiceOption) toolChoice;
            if (choice.equals(ToolChoiceOption.NONE)) {
                return Arrays.asList();
            } else {
                return getToolFunctions();
            }
        } else if (toolChoice instanceof ToolChoice) {
            var functionName = ((ToolChoice) toolChoice).getFunction().getName();
            if (!mapFunctions.containsKey(functionName)) {
                throw new SimpleOpenAIException("The function {0} was not enrolled in the executor.", functionName,
                        null);
            }
            return Arrays.asList(Tool.function(mapFunctions.get(functionName)));
        } else {
            throw new SimpleOpenAIException("The object {0} is of an unexpected type.", toolChoice.toString(), null);
        }
    }

    public void enrollFunction(FunctionDef function) {
        mapFunctions.put(function.getName(), function);
    }

    public void enrollFunctions(List<FunctionDef> functions) {
        if (functions == null) {
            throw new SimpleOpenAIException("No functions were entered.", "", null);
        }
        mapFunctions.clear();
        functions.forEach(this::enrollFunction);
    }

    @SuppressWarnings("unchecked")
    public <T> T execute(FunctionCall functionCall) {
        if (functionCall == null || CommonUtil.isNullOrEmpty(functionCall.getName())) {
            throw new SimpleOpenAIException("No function was entered or it does not has a name.", "", null);
        }
        var functionName = functionCall.getName();
        if (!mapFunctions.containsKey(functionName)) {
            throw new SimpleOpenAIException("The function {0} was not enrolled in the executor.", functionName,
                    null);
        }
        try {
            var function = mapFunctions.get(functionName);
            var object = JsonUtil.jsonToObject(
                    functionCall.getArguments().isBlank() ? "{}" : functionCall.getArguments(),
                    function.getFunctionalClass());
            return (T) object.execute();
        } catch (RuntimeException e) {
            throw new SimpleOpenAIException("Cannot execute the function {0}.", functionName, e);
        }
    }

    /**
     * Run the 'execute()' method for a list of FunctionDefs.
     * 
     * @param <R>            Specific type to gather the result. ToolMessage for ChatCompletion or
     *                       ToolOutput for Assistants.
     * @param toolCalls      Response from the model to call functions.
     * @param toolOutputItem BiFunction with two arguments: 'toolCallId' and 'result'. Returns a new R
     *                       object with those arguments.
     * @return List of R objects.
     */
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
