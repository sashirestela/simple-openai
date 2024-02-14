package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class ThreadRequest {

    @Singular
    private List<ThreadMessageRequest> messages;
    private Map<String, String> metadata;

}