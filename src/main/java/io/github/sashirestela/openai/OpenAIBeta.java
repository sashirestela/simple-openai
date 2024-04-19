package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.Event;
import io.github.sashirestela.cleverclient.annotation.Body;
import io.github.sashirestela.cleverclient.annotation.DELETE;
import io.github.sashirestela.cleverclient.annotation.GET;
import io.github.sashirestela.cleverclient.annotation.Header;
import io.github.sashirestela.cleverclient.annotation.POST;
import io.github.sashirestela.cleverclient.annotation.Path;
import io.github.sashirestela.cleverclient.annotation.Query;
import io.github.sashirestela.cleverclient.annotation.Resource;
import io.github.sashirestela.openai.domain.OpenAIDeletedResponse;
import io.github.sashirestela.openai.domain.Page;
import io.github.sashirestela.openai.domain.PageRequest;
import io.github.sashirestela.openai.domain.assistant.Assistant;
import io.github.sashirestela.openai.domain.assistant.AssistantFile;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantStreamEvents;
import io.github.sashirestela.openai.domain.assistant.FilePath;
import io.github.sashirestela.openai.domain.assistant.Thread;
import io.github.sashirestela.openai.domain.assistant.ThreadCreateAndRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessage;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageFile;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageModifyRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadModifyRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.ThreadRunModifyRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStep;
import io.github.sashirestela.openai.domain.assistant.ToolOutput;
import io.github.sashirestela.openai.domain.assistant.ToolOutputSubmission;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * The Assistants API allows you to build AI assistants within your own applications. An Assistant
 * has instructions and can leverage models, tools, and knowledge to respond to user queries. The
 * Assistants API currently supports three types of tools: Code Interpreter, Retrieval, and Function
 * calling.
 */
public interface OpenAIBeta {

    /**
     * Build assistants that can call models and use tools to perform tasks.
     *
     * @see <a href= "https://platform.openai.com/docs/api-reference/assistants">OpenAI Assistants</a>
     */
    @Resource("/v1/assistants")
    @Header(name = "OpenAI-Beta", value = "assistants=v1")
    interface Assistants {

        /**
         * Create an assistant with a model and instructions.
         *
         * @param assistantRequest The assistant request.
         * @return the created assistant object
         */
        @POST
        CompletableFuture<Assistant> create(@Body AssistantRequest assistantRequest);

        /**
         * Retrieves an assistant.
         *
         * @param assistantId The ID of the assistant to retrieve.
         * @return The {@link Assistant} object matching the specified ID.
         */
        @GET("/{assistantId}")
        CompletableFuture<Assistant> getOne(@Path("assistantId") String assistantId);

        /**
         * Modifies an assistant.
         *
         * @param assistantId      The ID of the assistant to retrieve.
         * @param assistantRequest The assistant request.
         * @return the modified assistant object
         */
        @POST("/{assistantId}")
        CompletableFuture<Assistant> modify(@Path("assistantId") String assistantId,
                @Body AssistantRequest assistantRequest);

        /**
         * Deletes an assistant.
         *
         * @param assistantId The ID of the assistant to delete.
         * @return the deletion status
         */
        @DELETE("/{assistantId}")
        CompletableFuture<OpenAIDeletedResponse> delete(@Path("assistantId") String assistantId);

        /**
         * Returns a list of assistants (first page only).
         *
         * @return the list of assistant objects
         */
        default CompletableFuture<Page<Assistant>> getList() {
            return getList(PageRequest.builder().build());
        }

        /**
         * Returns a list of assistants.
         *
         * @param page The result page requested.
         * @return the list of assistant objects
         */
        @GET
        CompletableFuture<Page<Assistant>> getList(@Query PageRequest page);

        /**
         * Create an assistant file by attaching a File to an assistant.
         *
         * @param assistantId The ID of the assistant for which to create a File.
         * @param fileId      A File ID (with purpose="assistants") that the assistant should use.
         * @return the created assistant file object.
         */
        default CompletableFuture<AssistantFile> createFile(String assistantId, String fileId) {
            return createFile(assistantId, FilePath.of(fileId));
        }

        /**
         * Create an assistant file by attaching a File to an assistant.
         *
         * @param assistantId The ID of the assistant for which to create a File.
         * @param file        A File ID (with purpose="assistants") that the assistant should use.
         * @return the created assistant file object.
         */
        @POST("/{assistantId}/files")
        CompletableFuture<AssistantFile> createFile(@Path("assistantId") String assistantId, @Body FilePath file);

        /**
         * Retrieves an AssistantFile.
         *
         * @param assistantId The ID of the assistant who the file belongs to.
         * @param fileId      The ID of the file we're getting.
         * @return the assistant file object matching the specified ID
         */
        @GET("/{assistantId}/files/{fileId}")
        CompletableFuture<AssistantFile> getFile(@Path("assistantId") String assistantId,
                @Path("fileId") String fileId);

        /**
         * Delete an assistant file.
         *
         * @param assistantId The ID of the assistant that the file belongs to.
         * @param fileId      The ID of the file to delete.
         * @return the deletion status
         */
        @DELETE("/{assistantId}/files/{fileId}")
        CompletableFuture<OpenAIDeletedResponse> deleteFile(@Path("assistantId") String assistantId,
                @Path("fileId") String fileId);

        /**
         * Returns a list of assistant files (first page only).
         *
         * @param assistantId The ID of the assistant the file belongs to.
         * @return the list of assistant file objects.
         */
        default CompletableFuture<Page<AssistantFile>> getFileList(String assistantId) {
            return getFileList(assistantId, PageRequest.builder().build());
        }

        /**
         * Returns a list of assistant files.
         *
         * @param assistantId The ID of the assistant the file belongs to.
         * @param page        The requested result page.
         * @return the list of assistant file objects.
         */
        @GET("/{assistantId}/files")
        CompletableFuture<Page<AssistantFile>> getFileList(@Path("assistantId") String assistantId,
                @Query PageRequest page);

    }

    /**
     * Build assistants that can call models and use tools to perform tasks.
     *
     * @see <a href="https://platform.openai.com/docs/api-reference/threads">OpenAI Threads</a>
     */
    @Resource("/v1/threads")
    @Header(name = "OpenAI-Beta", value = "assistants=v1")
    interface Threads {

        /**
         * Creates a message thread.
         *
         * @return the created thread object
         */
        default CompletableFuture<Thread> create() {
            return create(ThreadRequest.builder().build());
        }

        /**
         * Creates a message thread.
         *
         * @param threadRequest The thread request.
         * @return the created thread object
         */
        @POST
        CompletableFuture<Thread> create(@Body ThreadRequest threadRequest);

        /**
         * Retrieves a thread.
         *
         * @param threadId The ID of the thread to retrieve.
         * @return The {@link Thread} object matching the specified ID.
         */
        @GET("/{threadId}")
        CompletableFuture<Thread> getOne(@Path("threadId") String threadId);

        /**
         * Modifies a thread.
         *
         * @param threadId      The ID of the thread to modify.
         * @param threadRequest The thread request.
         * @return the created thread object
         */
        @POST("/{threadId}")
        CompletableFuture<Thread> modify(@Path("threadId") String threadId, @Body ThreadModifyRequest threadRequest);

        /**
         * Deletes a thread.
         *
         * @param threadId The ID of the thread to delete.
         * @return the thread deletion status
         */
        @DELETE("/{threadId}")
        CompletableFuture<OpenAIDeletedResponse> delete(@Path("threadId") String threadId);

        /**
         * Create a message.
         *
         * @param threadId The ID of the thread to create a message for.
         * @param request  The requested message to create.
         * @return the created message object
         */
        @POST("/{threadId}/messages")
        CompletableFuture<ThreadMessage> createMessage(@Path("threadId") String threadId,
                @Body ThreadMessageRequest request);

        /**
         * Retrieve a message.
         *
         * @param threadId  The ID of the thread to which this message belongs.
         * @param messageId The ID of the message to retrieve.
         * @return The message object matching the specified ID.
         */
        @GET("/{threadId}/messages/{messageId}")
        CompletableFuture<ThreadMessage> getMessage(@Path("threadId") String threadId,
                @Path("messageId") String messageId);

        /**
         * Modifies a message.
         *
         * @param threadId  The ID of the thread to which this message belongs.
         * @param messageId The ID of the message to modify.
         * @param request   The message modification request.
         * @return The message object matching the specified ID.
         */
        @POST("/{threadId}/messages/{messageId}")
        CompletableFuture<ThreadMessage> modifyMessage(@Path("threadId") String threadId,
                @Path("messageId") String messageId, @Body ThreadMessageModifyRequest request);

        /**
         * Returns a list of messages for a given thread (first page only).
         *
         * @param threadId The ID of the thread the messages belong to.
         * @return The list of message objects.
         */
        default CompletableFuture<Page<ThreadMessage>> getMessageList(String threadId) {
            return getMessageList(threadId, PageRequest.builder().build(), null);
        }

        /**
         * Returns a list of messages for a given thread.
         *
         * @param threadId The ID of the thread the messages belong to.
         * @param page     The requested result page.
         * @param runId    Filter messages by the run ID that generated them.
         * @return The list of message objects.
         */
        @GET("/{threadId}/messages")
        CompletableFuture<Page<ThreadMessage>> getMessageList(@Path("threadId") String threadId,
                @Query PageRequest page, @Query("run_id") String runId);

        /**
         * Deletes a message.
         *
         * @param threadId  The ID of the thread to which this message belongs.
         * @param messageId The ID of the message to delete.
         * @return The thread message deletion status.
         */
        @POST("/{threadId}/messages/{messageId}")
        CompletableFuture<OpenAIDeletedResponse> deleteMessage(@Path("threadId") String threadId,
                @Path("messageId") String messageId);

        /**
         * Retrieves a message file.
         *
         * @param threadId  The ID of the thread to which the message and File belong.
         * @param messageId The ID of the message the file belongs to.
         * @param fileId    The ID of the file being retrieved.
         * @return The message file object.
         */
        @GET("/{threadId}/messages/{messageId}/files/{fileId}")
        CompletableFuture<ThreadMessageFile> getMessageFile(@Path("threadId") String threadId,
                @Path("messageId") String messageId, @Path("fileId") String fileId);

        /**
         * Returns a list of message files (first page only).
         *
         * @param threadId  The ID of the thread to which the message and File belong.
         * @param messageId The ID of the message the file belongs to.
         * @return The list of message file objects.
         */
        default CompletableFuture<Page<ThreadMessageFile>> getMessageFileList(String threadId, String messageId) {
            return getMessageFileList(threadId, messageId, PageRequest.builder().build());
        }

        /**
         * Returns a list of message files.
         *
         * @param threadId  The ID of the thread to which the message and File belong.
         * @param messageId The ID of the message the file belongs to.
         * @param page      The requested result page.
         * @return The list of message file objects.
         */
        @GET("/{threadId}/messages/{messageId}/files")
        CompletableFuture<Page<ThreadMessageFile>> getMessageFileList(@Path("threadId") String threadId,
                @Path("messageId") String messageId, @Query PageRequest page);

        /**
         * Create a run.
         *
         * @param threadId    The ID of the thread to run.
         * @param assistantId The ID of the assistant to use to execute this run.
         * @return the queued run object
         */
        default CompletableFuture<ThreadRun> createRun(String threadId, String assistantId) {
            return createRunRoot(threadId, ThreadRunRequest.builder()
                    .assistantId(assistantId)
                    .stream(Boolean.FALSE)
                    .build());
        }

        /**
         * Create a run.
         *
         * @param threadId The ID of the thread to run.
         * @param request  The requested run.
         * @return the queued run object
         */
        default CompletableFuture<ThreadRun> createRun(@Path("threadId") String threadId,
                @Body ThreadRunRequest request) {
            var newRequest = request.withStream(Boolean.FALSE);
            return createRunRoot(threadId, newRequest);
        }

        @POST("/{threadId}/runs")
        CompletableFuture<ThreadRun> createRunRoot(@Path("threadId") String threadId, @Body ThreadRunRequest request);

        /**
         * Create a run and stream the response.
         *
         * @param threadId The ID of the thread to run.
         * @param request  The requested run.
         * @return A stream of events.
         */
        default CompletableFuture<Stream<Event>> createRunStream(@Path("threadId") String threadId,
                @Body ThreadRunRequest request) {
            var newRequest = request.withStream(Boolean.TRUE);
            return createRunStreamRoot(threadId, newRequest);
        }

        @POST("/{threadId}/runs")
        @AssistantStreamEvents
        CompletableFuture<Stream<Event>> createRunStreamRoot(@Path("threadId") String threadId,
                @Body ThreadRunRequest request);

        /**
         * Retrieves a run.
         *
         * @param threadId The ID of the thread that was run.
         * @param runId    The ID of the run to retrieve.
         * @return The run object matching the specified ID.
         */
        @GET("/{threadId}/runs/{runId}")
        CompletableFuture<ThreadRun> getRun(@Path("threadId") String threadId, @Path("runId") String runId);

        /**
         * Modifies a run.
         *
         * @param threadId The ID of the thread that was run.
         * @param runId    The ID of the run to modify.
         * @param request  The requested run.
         * @return The modified run object matching the specified ID.
         */
        @POST("/{threadId}/runs/{runId}")
        CompletableFuture<ThreadRun> modifyRun(@Path("threadId") String threadId, @Path("runId") String runId,
                @Body ThreadRunModifyRequest request);

        /**
         * Returns a list of runs belonging to a thread (first page).
         *
         * @param threadId The ID of the thread the run belongs to.
         * @return A list of run objects.
         */
        default CompletableFuture<Page<ThreadRun>> getRunList(String threadId) {
            return getRunList(threadId, PageRequest.builder().build());
        }

        /**
         * Returns a list of runs belonging to a thread.
         *
         * @param threadId The ID of the thread the run belongs to.
         * @param page     The requested page of result.
         * @return A list of run objects.
         */
        @GET("/{threadId}/runs")
        CompletableFuture<Page<ThreadRun>> getRunList(@Path("threadId") String threadId, @Query PageRequest page);

        /**
         * Submit tool outputs to run.
         *
         * @param threadId    The ID of the thread to which this run belongs.
         * @param runId       The ID of the run that requires the tool output submission.
         * @param toolOutputs The tool output submission.
         * @return The modified run object matching the specified ID.
         */
        default CompletableFuture<ThreadRun> submitToolOutputs(String threadId, String runId,
                List<ToolOutput> toolOutputs) {
            return submitToolOutputs(threadId, runId, ToolOutputSubmission.builder()
                    .toolOutputs(toolOutputs)
                    .build());
        }

        /**
         * Submit tool outputs to run.
         *
         * @param threadId    The ID of the thread to which this run belongs.
         * @param runId       The ID of the run that requires the tool output submission.
         * @param toolOutputs The tool output submission.
         * @return The modified run object matching the specified ID.
         */
        default CompletableFuture<ThreadRun> submitToolOutputs(@Path("threadId") String threadId,
                @Path("runId") String runId, @Body ToolOutputSubmission toolOutputs) {
            var newToolOutputs = toolOutputs.withStream(Boolean.FALSE);
            return submitToolOutputsRoot(threadId, runId, newToolOutputs);
        }

        @POST("/{threadId}/runs/{runId}/submit_tool_outputs")
        CompletableFuture<ThreadRun> submitToolOutputsRoot(@Path("threadId") String threadId,
                @Path("runId") String runId,
                @Body ToolOutputSubmission toolOutputs);

        /**
         * Submit tool outputs to run and stream the response.
         *
         * @param threadId    The ID of the thread to which this run belongs.
         * @param runId       The ID of the run that requires the tool output submission.
         * @param toolOutputs The tool output submission.
         * @return A stream of events.
         */
        default CompletableFuture<Stream<Event>> submitToolOutputsStream(@Path("threadId") String threadId,
                @Path("runId") String runId, @Body ToolOutputSubmission toolOutputs) {
            var newToolOutputs = toolOutputs.withStream(Boolean.TRUE);
            return submitToolOutputsStreamRoot(threadId, runId, newToolOutputs);
        }

        @POST("/{threadId}/runs/{runId}/submit_tool_outputs")
        @AssistantStreamEvents
        CompletableFuture<Stream<Event>> submitToolOutputsStreamRoot(@Path("threadId") String threadId,
                @Path("runId") String runId, @Body ToolOutputSubmission toolOutputs);

        /**
         * Cancels a run that is {@code in_progress}.
         *
         * @param threadId The ID of the thread to which this run belongs.
         * @param runId    The ID of the run to cancel.
         * @return The modified run object matching the specified ID.
         */
        @POST("/{threadId}/runs/{runId}/cancel")
        CompletableFuture<ThreadRun> cancelRun(@Path("threadId") String threadId, @Path("runId") String runId);

        /**
         * Create a thread and run it in one request.
         *
         * @param request The thread request create and to run.
         * @return A created run object.
         */
        default CompletableFuture<ThreadRun> createThreadAndRun(@Body ThreadCreateAndRunRequest request) {
            var newRequest = request.withStream(Boolean.FALSE);
            return createThreadAndRunRoot(newRequest);
        }

        @POST("/runs")
        CompletableFuture<ThreadRun> createThreadAndRunRoot(@Body ThreadCreateAndRunRequest request);

        /**
         * Create a thread and run it in one request and stream the response.
         *
         * @param request The thread request create and to run.
         * @return A stream of events.
         */
        default CompletableFuture<Stream<Event>> createThreadAndRunStream(@Body ThreadCreateAndRunRequest request) {
            var newRequest = request.withStream(Boolean.TRUE);
            return createThreadAndRunStreamRoot(newRequest);
        }

        @POST("/runs")
        @AssistantStreamEvents
        CompletableFuture<Stream<Event>> createThreadAndRunStreamRoot(@Body ThreadCreateAndRunRequest request);

        /**
         * Retrieves a run step.
         *
         * @param threadId The ID of the thread the run and run steps belong to.
         * @param runId    The ID of the run steps belong to.
         * @param stepId   The ID of the run step to retrieve.
         * @return the list of run step objects
         */
        @GET("/{threadId}/runs/{runId}/steps/{stepId}")
        CompletableFuture<ThreadRunStep> getRunStep(@Path("threadId") String threadId, @Path("runId") String runId,
                @Path("stepId") String stepId);

        /**
         * Returns a list of run steps belonging to a run.
         *
         * @param threadId The ID of the thread the run and run steps belong to.
         * @param runId    The ID of the run steps belong to.
         * @return the list of run step objects
         */
        default CompletableFuture<Page<ThreadRunStep>> getRunStepList(String threadId, String runId) {
            return getRunStepList(threadId, runId, PageRequest.builder().build());
        }

        /**
         * Returns a list of run steps belonging to a run.
         *
         * @param threadId The ID of the thread the run and run steps belong to.
         * @param runId    The ID of the run steps belong to.
         * @param page     The requested result page.
         * @return the list of run step objects
         */
        @GET("/{threadId}/runs/{runId}/steps")
        CompletableFuture<Page<ThreadRunStep>> getRunStepList(@Path("threadId") String threadId,
                @Path("runId") String runId, @Query PageRequest page);

    }

}
