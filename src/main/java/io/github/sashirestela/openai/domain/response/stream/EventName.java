package io.github.sashirestela.openai.domain.response.stream;

public final class EventName {

    private EventName() {
    }

    public static final String RESPONSE_CREATED = "response.created";
    public static final String RESPONSE_IN_PROGRESS = "response.in_progress";
    public static final String RESPONSE_COMPLETED = "response.completed";
    public static final String RESPONSE_FAILED = "response.failed";
    public static final String RESPONSE_INCOMPLETE = "response.incomplete";

    public static final String RESPONSE_OUTPUT_ITEM_ADDED = "response.output_item.added";
    public static final String RESPONSE_OUTPUT_ITEM_DONE = "response.output_item.done";

    public static final String RESPONSE_CONTENT_PART_ADDED = "response.content_part.added";
    public static final String RESPONSE_CONTENT_PART_DONE = "response.content_part.done";

    public static final String RESPONSE_OUTPUT_TEXT_ANNOTATION_ADDED = "response.output_text.annotation.added";

    public static final String RESPONSE_OUTPUT_TEXT_DELTA = "response.output_text.delta";
    public static final String RESPONSE_OUTPUT_TEXT_DONE = "response.output_text.done";

    public static final String RESPONSE_REFUSAL_DELTA = "response.refusal.delta";
    public static final String RESPONSE_REFUSAL_DONE = "response.refusal.done";

    public static final String RESPONSE_FUNCTION_CALL_ARGUMENTS_DELTA = "response.function_call_arguments.delta";
    public static final String RESPONSE_FUNCTION_CALL_ARGUMENTS_DONE = "response.function_call_arguments.done";

    public static final String RESPONSE_FILE_SEARCH_CALL_IN_PROGRESS = "response.file_search_call.in_progress";
    public static final String RESPONSE_FILE_SEARCH_CALL_SEARCHING = "response.file_search_call.searching";
    public static final String RESPONSE_FILE_SEARCH_CALL_COMPLETED = "response.file_search_call.completed";

    public static final String RESPONSE_WEB_SEARCH_CALL_IN_PROGRESS = "response.web_search_call.in_progress";
    public static final String RESPONSE_WEB_SEARCH_CALL_SEARCHING = "response.web_search_call.searching";
    public static final String RESPONSE_WEB_SEARCH_CALL_COMPLETED = "response.web_search_call.completed";

    public static final String RESPONSE_REASONING_SUMMARY_PART_ADDED = "response.reasoning_summary_part.added";
    public static final String RESPONSE_REASONING_SUMMARY_PART_DONE = "response.reasoning_summary_part.done";

    public static final String RESPONSE_REASONING_SUMMARY_TEXT_DELTA = "response.reasoning_summary_text.delta";
    public static final String RESPONSE_REASONING_SUMMARY_TEXT_DONE = "response.reasoning_summary_text.done";

    public static final String RESPONSE_ERROR = "error";

}
