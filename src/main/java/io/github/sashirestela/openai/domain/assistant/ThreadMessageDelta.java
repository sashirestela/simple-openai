package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThreadMessageDelta {

    private String id;
    private String object;
    private ThreadMessageDeltaDetail delta;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public class ThreadMessageDeltaDetail {

        private String role;
        private List<ThreadMessageContent> content;
        private List<String> fileIds;

    }

}
