package io.github.sashirestela.openai.domain.moderation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class ModerationResponse {

    private String id;
    private String model;
    private List<Moderation> results;

}
