package io.github.sashirestela.openai.domain.realtime;

public final class Realtime {

    private Realtime() {
    }

    public static final String SESSION_UPDATE = "session.update";
    public static final String INPUT_AUDIO_BUFFER_APPEND = "input_audio_buffer.append";
    public static final String INPUT_AUDIO_BUFFER_COMMIT = "input_audio_buffer.commit";
    public static final String INPUT_AUDIO_BUFFER_CLEAR = "input_audio_buffer.clear";
    public static final String CONVERSATION_ITEM_CREATE = "conversation.item.create";
    public static final String CONVERSATION_ITEM_TRUNCATE = "conversation.item.truncate";
    public static final String CONVERSATION_ITEM_DELETE = "conversation.item.delete";
    public static final String RESPONSE_CREATE = "response.create";
    public static final String RESPONSE_CANCEL = "response.cancel";

    public static final String ERROR = "error";
    public static final String SESSION_CREATED = "session.created";
    public static final String SESSION_UPDATED = "session.updated";
    public static final String CONVERSATION_CREATED = "conversation.created";
    public static final String CONVERSATION_ITEM_CREATED = "conversation.item.created";
    public static final String CONVERSATION_ITEM_AUDIO_TRANS_COMPLETED = "conversation.item.input_audio_transcription.completed";
    public static final String CONVERSATION_ITEM_AUDIO_TRANS_FAILED = "conversation.item.input_audio_transcription.failed";
    public static final String CONVERSATION_ITEM_TRUNCATED = "conversation.item.truncated";
    public static final String CONVERSATION_ITEM_DELETED = "conversation.item.deleted";
    public static final String INPUT_AUDIO_BUFFER_COMMITTED = "input_audio_buffer.committed";
    public static final String INPUT_AUDIO_BUFFER_CLEARED = "input_audio_buffer.cleared";
    public static final String INPUT_AUDIO_BUFFER_SPEECH_STARTED = "input_audio_buffer.speech_started";
    public static final String INPUT_AUDIO_BUFFER_SPEECH_STOPPED = "input_audio_buffer.speech_stopped";
    public static final String RESPONSE_CREATED = "response.created";
    public static final String RESPONSE_DONE = "response.done";
    public static final String RESPONSE_OUTPUT_ITEM_ADDED = "response.output_item.added";
    public static final String RESPONSE_OUTPUT_ITEM_DONE = "response.output_item.done";
    public static final String RESPONSE_CONTENT_PART_ADDED = "response.content_part.added";
    public static final String RESPONSE_CONTENT_PART_DONE = "response.content_part.done";
    public static final String RESPONSE_TEXT_DELTA = "response.text.delta";
    public static final String RESPONSE_TEXT_DONE = "response.text.done";
    public static final String RESPONSE_AUDIO_TRANSCRIPT_DELTA = "response.audio_transcript.delta";
    public static final String RESPONSE_AUDIO_TRANSCRIPT_DONE = "response.audio_transcript.done";
    public static final String RESPONSE_AUDIO_DELTA = "response.audio.delta";
    public static final String RESPONSE_AUDIO_DONE = "response.audio.done";
    public static final String RESPONSE_FUNCTION_CALL_ARGS_DELTA = "response.function_call_arguments.delta";
    public static final String RESPONSE_FUNCTION_CALL_ARGS_DONE = "response.function_call_arguments.done";
    public static final String RATE_LIMITS_UPDATED = "rate_limits.updated";

}
