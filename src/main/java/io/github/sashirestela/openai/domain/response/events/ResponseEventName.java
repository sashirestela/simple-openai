package io.github.sashirestela.openai.domain.response.events;

/**
 * Event names for Response API streaming events.
 */
public final class ResponseEventName {

    private ResponseEventName() {
    }

    /**
     * Event when a response is initially created.
     */
    public static final String RESPONSE_CREATED = "response.created";

    /**
     * Event when a response is in progress of being generated.
     */
    public static final String RESPONSE_IN_PROGRESS = "response.in_progress";

    /**
     * Event when an output item is added to the response.
     */
    public static final String RESPONSE_OUTPUT_ITEM_ADDED = "response.output_item.added";

    /**
     * Event when a content part is added to an output item.
     */
    public static final String RESPONSE_CONTENT_PART_ADDED = "response.content_part.added";

    /**
     * Event when an output text delta (incremental text) is received.
     */
    public static final String RESPONSE_OUTPUT_TEXT_DELTA = "response.output_text.delta";

    /**
     * Event when an output text is completed.
     */
    public static final String RESPONSE_OUTPUT_TEXT_DONE = "response.output_text.done";

    /**
     * Event when a content part is completed.
     */
    public static final String RESPONSE_CONTENT_PART_DONE = "response.content_part.done";

    /**
     * Event when an output item is completed.
     */
    public static final String RESPONSE_OUTPUT_ITEM_DONE = "response.output_item.done";

    /**
     * Event when a response is completed.
     */
    public static final String RESPONSE_COMPLETED = "response.completed";

    /**
     * Event when an error occurs.
     */
    public static final String ERROR = "error";

}
