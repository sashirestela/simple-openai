package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Item {

    private String id;

    private ItemType type;

    private String object;

    private String status;

    private RoleItemMessage role;

    @Singular("contentItem")
    private List<ContentItem> content;

    private String callId;

    private String name;

    private String arguments;

    private String output;

    public enum ItemType {

        @JsonProperty("message")
        MESSAGE,

        @JsonProperty("function_call")
        FUNCTION_CALL,

        @JsonProperty("function_call_output")
        FUNCTION_CALL_OUTPUT;

    }

    public enum RoleItemMessage {

        @JsonProperty("system")
        SYSTEM,

        @JsonProperty("user")
        USER,

        @JsonProperty("assistant")
        ASSISTANT;

    }

    public enum ContentItemType {

        @JsonProperty("input_text")
        INPUT_TEXT,

        @JsonProperty("input_audio")
        INPUT_AUDIO,

        @JsonProperty("text")
        TEXT,

        @JsonProperty("audio")
        AUDIO,

        @JsonProperty("item_reference")
        ITEM_REFERENCE;

    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContentItem {

        private ContentItemType type;
        private String text;
        private String id;
        private String audio;
        private String transcript;

    }

}
