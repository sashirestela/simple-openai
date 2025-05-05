package io.github.sashirestela.openai.domain.response.events;

import lombok.Getter;

/**
 * Represents the completion of an output text in a Response API streaming event.
 */
@Getter
public class ResponseOutputText {

    /**
     * The event type.
     */
    private String type;

    /**
     * The ID of the item this output text belongs to.
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
     * The complete text content.
     */
    private String text;

}
