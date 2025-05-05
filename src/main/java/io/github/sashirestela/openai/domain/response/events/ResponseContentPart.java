package io.github.sashirestela.openai.domain.response.events;

import lombok.Getter;

import java.util.Map;

/**
 * Represents a content part added to an output item in a Response API streaming event.
 */
@Getter
public class ResponseContentPart {

    /**
     * The event type.
     */
    private String type;

    /**
     * The ID of the item this content part belongs to.
     */
    private String itemId;

    /**
     * The index of the output in the response.
     */
    private String outputIndex;

    /**
     * The index of the content part in the output item.
     */
    private String contentIndex;

    /**
     * The part data.
     */
    private Map<String, Object> part;

}
