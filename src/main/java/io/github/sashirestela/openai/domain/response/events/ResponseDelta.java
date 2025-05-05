package io.github.sashirestela.openai.domain.response.events;

import lombok.Getter;
import lombok.ToString;

/**
 * Represents an incremental text update in a Response API streaming event.
 */
@Getter
@ToString
public class ResponseDelta {

    /**
     * The event type.
     */
    private String type;

    /**
     * The incremental text content.
     */
    private String delta;

    /**
     * The ID of the item this delta belongs to.
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

}
