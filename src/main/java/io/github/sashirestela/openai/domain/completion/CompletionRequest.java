package io.github.sashirestela.openai.domain.completion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.SimpleUncheckedException;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;

@Getter
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompletionRequest {

    @NonNull private String model;
    @NonNull private Object prompt;
    private String suffix;
    private Integer maxTokens;
    private Double temperature;
    private Double topP;
    private Integer n;
    @With private Boolean stream;
    private Integer logprobs;
    private Boolean echo;
    private Object stop;
    private Integer seed;
    private Double presencePenalty;
    private Double frequencyPenalty;
    private Integer bestOf;
    private Map<String, Integer> logitBias;
    private String user;

    @Builder
    public CompletionRequest(@NonNull String model, @NonNull Object prompt, String suffix, Integer maxTokens,
            Double temperature, Double topP, Integer n, Boolean stream, Integer logprobs, Boolean echo, Object stop,
            Integer seed, Double presencePenalty, Double frequencyPenalty, Integer bestOf,
            Map<String, Integer> logitBias, String user) {
        if (!(prompt instanceof String) && !(prompt instanceof List
                && (((List<?>) prompt).get(0) instanceof String || ((List<?>) prompt).get(0) instanceof Integer
                        || (((List<?>) prompt).get(0) instanceof List
                                && (((List<?>) ((List<?>) prompt).get(0)).get(0) instanceof Integer))))) {
            throw new SimpleUncheckedException(
                    "The field prompt must be String or List<String> or List<Integer> or List<List<Integer>> classes.",
                    null, null);
        }
        if (stop != null && !(stop instanceof String) && !(stop instanceof List
                && ((List<?>) stop).get(0) instanceof String && ((List<?>) stop).size() <= 4)) {
            throw new SimpleUncheckedException(
                    "The field stop must be String or List<String> (max 4 items) classes.",
                    null, null);
        }
        this.model = model;
        this.prompt = prompt;
        this.suffix = suffix;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.topP = topP;
        this.n = n;
        this.stream = stream;
        this.logprobs = logprobs;
        this.echo = echo;
        this.stop = stop;
        this.seed = seed;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.bestOf = bestOf;
        this.logitBias = logitBias;
        this.user = user;
    }
}