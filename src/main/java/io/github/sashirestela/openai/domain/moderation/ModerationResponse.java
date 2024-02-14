package io.github.sashirestela.openai.domain.moderation;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ModerationResponse {

    private String id;
    private String model;
    private List<Moderation> results;

}