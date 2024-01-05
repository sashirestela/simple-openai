package io.github.sashirestela.openai.domain.embedding;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.github.sashirestela.openai.SimpleUncheckedException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;

@Getter
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmbeddingRequest {

    @NonNull private String model;
    @NonNull private Object input;
    @With private EncodingFormat encodingFormat;
    private String user;

    @Builder
    public EmbeddingRequest(@NonNull String model, @NonNull Object input, EncodingFormat encodingFormat, String user) {
        if (!(input instanceof String) && !(input instanceof List
                && (((List<?>) input).get(0) instanceof String || ((List<?>) input).get(0) instanceof Integer
                        || (((List<?>) input).get(0) instanceof List
                                && (((List<?>) ((List<?>) input).get(0)).get(0) instanceof Integer))))) {
            throw new SimpleUncheckedException(
                    "The field input must be String or List<String> or List<Integer> or List<List<Integer>> classes.",
                    null, null);
        }
        this.model = model;
        this.input = input;
        this.encodingFormat = encodingFormat;
        this.user = user;
    }
}