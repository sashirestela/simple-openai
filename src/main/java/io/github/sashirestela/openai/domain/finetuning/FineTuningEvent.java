package io.github.sashirestela.openai.domain.finetuning;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class FineTuningEvent {

    private String id;

    private String object;

    @JsonProperty("created_at")
    private Long createdAt;

    private String level;

    private String message;

    private String data;

    private String type;

}