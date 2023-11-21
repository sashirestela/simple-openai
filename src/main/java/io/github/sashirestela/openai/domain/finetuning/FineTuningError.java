package io.github.sashirestela.openai.domain.finetuning;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class FineTuningError {

    private String code;

    private String message;

    private String param;
}