package io.github.sashirestela.openai.domain.assistant;

public interface Events {

    static final String THREAD_CREATED = "thread.created";

    static final String THREAD_RUN_CREATED = "thread.run.created";
    static final String THREAD_RUN_QUEUED = "thread.run.queued";
    static final String THREAD_RUN_IN_PROGRESS = "thread.run.in_progress";
    static final String THREAD_RUN_REQUIRES_ACTION = "thread.run.requires_action";
    static final String THREAD_RUN_COMPLETED = "thread.run.completed";
    static final String THREAD_RUN_FAILED = "thread.run.failed";
    static final String THREAD_RUN_CANCELLING = "thread.run.cancelling";
    static final String THREAD_RUN_CANCELLED = "thread.run.cancelled";
    static final String THREAD_RUN_EXPIRED = "thread.run.expired";

    static final String THREAD_RUN_STEP_CREATED = "thread.run.step.created";
    static final String THREAD_RUN_STEP_IN_PROGRESS = "thread.run.step.in_progress";
    static final String THREAD_RUN_STEP_COMPLETED = "thread.run.step.completed";
    static final String THREAD_RUN_STEP_FAILED = "thread.run.step.failed";
    static final String THREAD_RUN_STEP_CANCELLED = "thread.run.step.cancelled";
    static final String THREAD_RUN_STEP_EXPIRED = "thread.run.step.expired";

    static final String THREAD_RUN_STEP_DELTA = "thread.run.step.delta";

    static final String THREAD_MESSAGE_CREATED = "thread.message.created";
    static final String THREAD_MESSAGE_IN_PROGRESS = "thread.message.in_progress";
    static final String THREAD_MESSAGE_COMPLETED = "thread.message.completed";
    static final String THREAD_MESSAGE_INCOMPLETE = "thread.message.incomplete";

    static final String THREAD_MESSAGE_DELTA = "thread.message.delta";

    static final String ERROR = "error";

}
