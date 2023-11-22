package io.github.sashirestela.openai.domain.embedding;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.github.sashirestela.openai.SimpleUncheckedException;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;

@Getter
public class EmbeddingRequest {

    @NonNull
    private String model;

    @NonNull
    private Object input;

    @With
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("encoding_format")
    private EncodingFormat encodingFormat;

    @JsonInclude(Include.NON_NULL)
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