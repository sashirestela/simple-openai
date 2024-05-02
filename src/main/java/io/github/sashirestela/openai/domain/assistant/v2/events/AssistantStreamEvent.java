package io.github.sashirestela.openai.domain.assistant.v2.events;

import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.ERROR;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_CREATED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_MESSAGE_COMPLETED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_MESSAGE_CREATED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_MESSAGE_DELTA;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_MESSAGE_INCOMPLETE;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_MESSAGE_IN_PROGRESS;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_CANCELLED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_CANCELLING;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_COMPLETED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_CREATED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_EXPIRED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_FAILED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_IN_PROGRESS;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_QUEUED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_REQUIRES_ACTION;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_STEP_CANCELLED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_STEP_COMPLETED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_STEP_CREATED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_STEP_DELTA;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_STEP_EXPIRED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_STEP_FAILED;
import static io.github.sashirestela.openai.domain.assistant.v2.events.EventName.THREAD_RUN_STEP_IN_PROGRESS;

import io.github.sashirestela.cleverclient.annotation.StreamType;
import io.github.sashirestela.openai.domain.assistant.v2.Thread;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadMessage;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadMessageDelta;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadRunStep;
import io.github.sashirestela.openai.domain.assistant.v2.ThreadRunStepDelta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@StreamType(type = Thread.class, events = { THREAD_CREATED })
@StreamType(type = ThreadRun.class, events = { THREAD_RUN_CREATED, THREAD_RUN_QUEUED, THREAD_RUN_IN_PROGRESS,
        THREAD_RUN_REQUIRES_ACTION, THREAD_RUN_COMPLETED, THREAD_RUN_FAILED, THREAD_RUN_CANCELLING,
        THREAD_RUN_CANCELLED, THREAD_RUN_EXPIRED })
@StreamType(type = ThreadRunStep.class, events = { THREAD_RUN_STEP_CREATED, THREAD_RUN_STEP_IN_PROGRESS,
        THREAD_RUN_STEP_COMPLETED, THREAD_RUN_STEP_FAILED, THREAD_RUN_STEP_CANCELLED, THREAD_RUN_STEP_EXPIRED })
@StreamType(type = ThreadRunStepDelta.class, events = { THREAD_RUN_STEP_DELTA })
@StreamType(type = ThreadMessage.class, events = { THREAD_MESSAGE_CREATED, THREAD_MESSAGE_IN_PROGRESS,
        THREAD_MESSAGE_COMPLETED, THREAD_MESSAGE_INCOMPLETE })
@StreamType(type = ThreadMessageDelta.class, events = { THREAD_MESSAGE_DELTA })
@StreamType(type = String.class, events = { ERROR })
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AssistantStreamEvent {
}
