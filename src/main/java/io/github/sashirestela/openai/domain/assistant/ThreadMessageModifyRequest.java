package io.github.sashirestela.openai.domain.assistant;

import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ThreadMessageModifyRequest {

    @Size(max = 16)
    private Map<String, String> metadata;

}
