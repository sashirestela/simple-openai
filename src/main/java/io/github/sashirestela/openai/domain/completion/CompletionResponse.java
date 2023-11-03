package io.github.sashirestela.openai.domain.completion;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.sashirestela.openai.domain.OpenAIUsage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class CompletionResponse {

    @JsonIgnore
    private String warning;

    private String id;

    private String object;

    private Long created;

    private String model;

    private List<Choice> choices;

    private OpenAIUsage usage;

    public String firstText() {
        return getChoices().get(0).getText();
    }

}