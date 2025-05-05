package io.github.sashirestela.openai.domain.response.events;

import lombok.Getter;

import java.util.Map;

/**
 * Represents an output item added to a response in a Response API streaming event.
 */
@Getter
public class ResponseOutputItem {

    /**
     * The event type.
     */
    private String type;

    /**
     * The index of the output in the response.
     */
    private String outputIndex;

    /**
     * The item data.
     */
    private Map<String, Object> item;

}
