package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public interface ThreadMessageId {

    String getId();

    @JsonCreator
    static ThreadMessageId of(String id) {
        return new Impl(id);
    }

    @AllArgsConstructor
    @Getter
    @ToString
    class Impl implements ThreadMessageId {

        @JsonProperty("message_id")
        public String id;

    }

}
