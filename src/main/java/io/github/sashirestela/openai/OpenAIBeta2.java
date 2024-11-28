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
import io.github.sashirestela.openai.common.DeletedObject;
import io.github.sashirestela.openai.common.Page;
import io.github.sashirestela.openai.common.PageRequest;
import io.github.sashirestela.openai.domain.assistant.Assistant;
import io.github.sashirestela.openai.domain.assistant.AssistantModifyRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.FileStatus;
import io.github.sashirestela.openai.domain.assistant.Thread;
import io.github.sashirestela.openai.domain.assistant.ThreadCreateAndRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessage;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageModifyRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadModifyRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.ThreadRun.RunStatus;
import io.github.sashirestela.openai.domain.assistant.ThreadRunModifyRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStep;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStepQuery;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest;
import io.github.sashirestela.openai.domain.assistant.VectorStore;
import io.github.sashirestela.openai.domain.assistant.VectorStore.VectorStoreStatus;
import io.github.sashirestela.openai.domain.assistant.VectorStoreFile;
import io.github.sashirestela.openai.domain.assistant.VectorStoreFileBatch;
import io.github.sashirestela.openai.domain.assistant.VectorStoreFileBatchRequest;
import io.github.sashirestela.openai.domain.assistant.VectorStoreFileRequest;
import io.github.sashirestela.openai.domain.assistant.VectorStoreModifyRequest;
import io.github.sashirestela.openai.domain.assistant.VectorStoreRequest;
import io.github.sashirestela.openai.domain.assistant.events.AssistantStreamEvent;
import io.github.sashirestela.openai.support.Constant;
import io.github.sashirestela.openai.support.Poller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * The Assistants API (Beta v2) allows you to build AI assistants within your own applications. An
 * Assistant has instructions and can leverage models, tools, and files to respond to user queries.
 * The Assistants API currently supports three types of tools: Code Interpreter, File Search, and
 * Function calling.
 */
public interface OpenAIBeta2 {

    /**
     * Build assistants that can call models and use tools to perform tasks.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/assistants">Assistants</a>
     */
    @Resource("/v1/assistants")
    @Header(name = Constant.OPENAI_BETA_HEADER, value = Constant.OPENAI_ASSISTANT_VERSION)
    interface Assistants {

        /**
         * Create an assistant with a model and instructions.
         * 
         * @param request The creation request.
         * @return The created assistant object.
         */
        @POST
        CompletableFuture<Assistant> create(@Body AssistantRequest request);

        /**
         * Create an assistant with a model.
         * 
         * @param model The assistant's model.
         * @return The created assistant object.
         */
        default CompletableFuture<Assistant> create(String model) {
            return create(AssistantRequest.builder().model(model).build());
        }

        /**
         * Returns a list of assistants.
         * 
         * @param page The page filter to narrow the list.
         * @return The list of assistants objects.
         */
        @GET
        CompletableFuture<Page<Assistant>> getList(@Query PageRequest page);

        /**
         * Returns a list of assistants (first page).
         * 
         * @return The list of assistants objects.
         */
        default CompletableFuture<Page<Assistant>> getList() {
            return getList(PageRequest.builder().build());
        }

        /**
         * Retrieves an assistant.
         * 
         * @param assistantId The ID of the assistant to retrieve.
         * @return The assistant object matching the specified ID.
         */
        @GET("/{assistantId}")
        CompletableFuture<Assistant> getOne(@Path("assistantId") String assistantId);

        /**
         * Modifies an assistant.
         * 
         * @param assistantId The ID of the assistant to modify.
         * @param request     The modification request.
         * @return The modified assistant object.
         */
        @POST("/{assistantId}")
        CompletableFuture<Assistant> modify(@Path("assistantId") String assistantId,
                @Body AssistantModifyRequest request);

        /**
         * Delete an assistant.
         * 
         * @param assistantId The ID of the assistant to delete.
         * @return Deletion status.
         */
        @DELETE("/{assistantId}")
        CompletableFuture<DeletedObject> delete(@Path("assistantId") String assistantId);

    }

    /**
     * Create threads that assistants can interact with.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/threads">Threads</a>
     */
    @Resource("/v1/threads")
    @Header(name = Constant.OPENAI_BETA_HEADER, value = Constant.OPENAI_ASSISTANT_VERSION)
    interface Threads {

        /**
         * Create a thread.
         * 
         * @param request The creation request.
         * @return A thread object.
         */
        @POST
        CompletableFuture<Thread> create(@Body ThreadRequest request);

        /**
         * Create a thread.
         * 
         * @return A thread object.
         */
        default CompletableFuture<Thread> create() {
            return create(ThreadRequest.builder().build());
        }

        /**
         * Retrieves a thread.
         * 
         * @param threadId The ID of the thread to retrieve.
         * @return The thread object matching the specified ID.
         */
        @GET("/{threadId}")
        CompletableFuture<Thread> getOne(@Path("threadId") String threadId);

        /**
         * Modifies a thread.
         * 
         * @param threadId The ID of the thread to modify.
         * @param request  The modification request.
         * @return The modified thread object matching the specified ID.
         */
        @POST("/{threadId}")
        CompletableFuture<Thread> modify(@Path("threadId") String threadId, @Body ThreadModifyRequest request);

        /**
         * Delete a thread.
         * 
         * @param threadId The ID of the thread to delete.
         * @return Deletion status.
         */
        @DELETE("/{threadId}")
        CompletableFuture<DeletedObject> delete(@Path("threadId") String threadId);

    }

    /**
     * Create messages within threads.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/messages">Thread Messages</a>
     */
    @Resource("/v1/threads/{threadId}/messages")
    @Header(name = Constant.OPENAI_BETA_HEADER, value = Constant.OPENAI_ASSISTANT_VERSION)
    interface ThreadMessages {

        /**
         * Create a message.
         * 
         * @param threadId The ID of the thread to create a message for.
         * @param request  The creation request.
         * @return A message object.
         */
        @POST
        CompletableFuture<ThreadMessage> create(@Path("threadId") String threadId, @Body ThreadMessageRequest request);

        /**
         * Returns a list of messages for a given thread.
         * 
         * @param threadId The ID of the thread the messages belong to.
         * @param page     The page filter to narrow the list.
         * @param runId    Filter messages by the run ID that generated them.
         * @return A list of message objects.
         */
        @GET
        CompletableFuture<Page<ThreadMessage>> getList(@Path("threadId") String threadId, @Query PageRequest page,
                @Query("run_id") String runId);

        /**
         * Returns a list of messages for a given thread (first page).
         * 
         * @param threadId The ID of the thread the messages belong to.
         * @return A list of message objects.
         */
        default CompletableFuture<Page<ThreadMessage>> getList(String threadId) {
            return getList(threadId, PageRequest.builder().build(), null);
        }

        /**
         * Retrieve a message.
         * 
         * @param threadId  The ID of the thread to which this message belongs.
         * @param messageId The ID of the message to retrieve.
         * @return The message object matching the specified ID.
         */
        @GET("/{messageId}")
        CompletableFuture<ThreadMessage> getOne(@Path("threadId") String threadId, @Path("messageId") String messageId);

        /**
         * Modifies a message.
         * 
         * @param threadId  The ID of the thread to which this message belongs.
         * @param messageId The ID of the message to modify.
         * @param request   The modification request.
         * @return The modified message object.
         */
        @POST("/{messageId}")
        CompletableFuture<ThreadMessage> modify(@Path("threadId") String threadId, @Path("messageId") String messageId,
                @Body ThreadMessageModifyRequest request);

        /**
         * Deletes a message.
         * 
         * @param threadId  The ID of the thread to which this message belongs.
         * @param messageId The ID of the message to delete.
         * @return Deletion status.
         */
        @DELETE("/{messageId}")
        CompletableFuture<DeletedObject> delete(@Path("threadId") String threadId, @Path("messageId") String messageId);

    }

    /**
     * Represents an execution run on a thread.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/runs">Thread Runs</a>
     */
    @Resource("/v1/threads")
    @Header(name = Constant.OPENAI_BETA_HEADER, value = Constant.OPENAI_ASSISTANT_VERSION)
    interface ThreadRuns {

        /**
         * Create a run (don't call it directly).
         * 
         * @param threadId The ID of the thread to run.
         * @param request  The creation request.
         * @return A run object.
         */
        @POST("/{threadId}/runs")
        CompletableFuture<ThreadRun> createPrimitive(@Path("threadId") String threadId, @Body ThreadRunRequest request);

        /**
         * Create a run without streaming.
         * 
         * @param threadId The ID of the thread to run.
         * @param request  The creation request.
         * @return A run object.
         */
        default CompletableFuture<ThreadRun> create(String threadId, ThreadRunRequest request) {
            var newRequest = request.withStream(Boolean.FALSE);
            return createPrimitive(threadId, newRequest);
        }

        /**
         * Create a run without streaming and poll until a terminal status is reached.
         * 
         * @param threadId The ID of the thread to run.
         * @param request  The creation request.
         * @return A run object (no CompletableFuture).
         */
        default ThreadRun createAndPoll(String threadId, ThreadRunRequest request) {
            var threadRun = create(threadId, request).join();
            return Poller.<ThreadRun>builder()
                    .timeUnit(TimeUnit.SECONDS)
                    .timeValue(1)
                    .queryMethod(tr -> getOne(tr.getThreadId(), tr.getId()).join())
                    .whileMethod(tr -> tr.getStatus().equals(RunStatus.IN_PROGRESS))
                    .build()
                    .execute(threadRun);
        }

        /**
         * Create a run with streaming (don't call it directly).
         * 
         * @param threadId The ID of the thread to run.
         * @param request  The creation request.
         * @return Stream of events that happen during the Run as server-sent events.
         */
        @POST("/{threadId}/runs")
        @AssistantStreamEvent
        CompletableFuture<Stream<Event>> createStreamPrimitive(@Path("threadId") String threadId,
                @Body ThreadRunRequest request);

        /**
         * Create a run by making sure to use streaming mode.
         * 
         * @param threadId The ID of the thread to run.
         * @param request  The creation request.
         * @return Stream of events that happen during the Run as server-sent events.
         */
        default CompletableFuture<Stream<Event>> createStream(String threadId, ThreadRunRequest request) {
            var newRequest = request.withStream(Boolean.TRUE);
            return createStreamPrimitive(threadId, newRequest);
        }

        /**
         * Create a thread and run it in one request (don't call it directly).
         * 
         * @param request The creation request.
         * @return A run object.
         */
        @POST("/runs")
        CompletableFuture<ThreadRun> createThreadAndRunPrimitive(@Body ThreadCreateAndRunRequest request);

        /**
         * Create a thread and run it in one request without streaming.
         * 
         * @param request The creation request.
         * @return A run object.
         */
        default CompletableFuture<ThreadRun> createThreadAndRun(ThreadCreateAndRunRequest request) {
            var newRequest = request.withStream(Boolean.FALSE);
            return createThreadAndRunPrimitive(newRequest);
        }

        /**
         * Create a thread and run it in one request without streaming and poll until a terminal status is
         * reached.
         * 
         * @param request The creation request.
         * @return A run object (no CompletableFuture).
         */
        default ThreadRun createThreadAndRunAndPoll(ThreadCreateAndRunRequest request) {
            var threadRun = createThreadAndRun(request).join();
            return Poller.<ThreadRun>builder()
                    .timeUnit(TimeUnit.SECONDS)
                    .timeValue(1)
                    .queryMethod(tr -> getOne(tr.getThreadId(), tr.getId()).join())
                    .whileMethod(tr -> tr.getStatus().equals(RunStatus.IN_PROGRESS))
                    .build()
                    .execute(threadRun);
        }

        /**
         * Create a thread and run it in one request with streaming (don't call it directly).
         * 
         * @param request The creation request.
         * @return Stream of events that happen during the Run as server-sent events.
         */
        @POST("/runs")
        @AssistantStreamEvent
        CompletableFuture<Stream<Event>> createThreadAndRunStreamPrimitive(@Body ThreadCreateAndRunRequest request);

        /**
         * Create a thread and run it in one request by making sure to use streaming mode.
         * 
         * @param request The creation request.
         * @return Stream of events that happen during the Run as server-sent events.
         */
        default CompletableFuture<Stream<Event>> createThreadAndRunStream(ThreadCreateAndRunRequest request) {
            var newRequest = request.withStream(Boolean.TRUE);
            return createThreadAndRunStreamPrimitive(newRequest);
        }

        /**
         * Returns a list of runs belonging to a thread.
         * 
         * @param threadId The ID of the thread the run belongs to.
         * @param page     The page filter to narrow the list.
         * @return A list of run objects.
         */
        @GET("/{threadId}/runs")
        CompletableFuture<Page<ThreadRun>> getList(@Path("threadId") String threadId, @Query PageRequest page);

        /**
         * Returns a list of runs belonging to a thread (first page).
         * 
         * @param threadId The ID of the thread the run belongs to.
         * @return A list of run objects.
         */
        default CompletableFuture<Page<ThreadRun>> getList(String threadId) {
            return getList(threadId, PageRequest.builder().build());
        }

        /**
         * Retrieves a run.
         * 
         * @param threadId The ID of the thread that was run.
         * @param runId    The ID of the run to retrieve.
         * @return The run object matching the specified ID.
         */
        @GET("/{threadId}/runs/{runId}")
        CompletableFuture<ThreadRun> getOne(@Path("threadId") String threadId, @Path("runId") String runId);

        /**
         * Modifies a run.
         * 
         * @param threadId The ID of the thread that was run.
         * @param runId    The ID of the run to modify.
         * @param request  The modification request.
         * @return The modified run object matching the specified ID.
         */
        @POST("/{threadId}/runs/{runId}")
        CompletableFuture<ThreadRun> modify(@Path("threadId") String threadId, @Path("runId") String runId,
                @Body ThreadRunModifyRequest request);

        /**
         * Cancels a run that is in_progress.
         * 
         * @param threadId The ID of the thread to which this run belongs.
         * @param runId    The ID of the run to cancel.
         * @return The modified run object matching the specified ID.
         */
        @POST("/{threadId}/runs/{runId}/cancel")
        CompletableFuture<ThreadRun> cancel(@Path("threadId") String threadId, @Path("runId") String runId);

        /**
         * Submit tool outputs to run (don't call it directly).
         * 
         * @param threadId The ID of the thread to which this run belongs.
         * @param runId    The ID of the run that requires the tool output submission.
         * @param request  The submission request.
         * @return The modified run object matching the specified ID.
         */
        @POST("/{threadId}/runs/{runId}/submit_tool_outputs")
        CompletableFuture<ThreadRun> submitToolOutputPrimitive(@Path("threadId") String threadId,
                @Path("runId") String runId, @Body ThreadRunSubmitOutputRequest request);

        /**
         * Submit tool outputs to run without streaming.
         * 
         * @param threadId The ID of the thread to which this run belongs.
         * @param runId    The ID of the run that requires the tool output submission.
         * @param request  The submission request.
         * @return The modified run object matching the specified ID.
         */
        default CompletableFuture<ThreadRun> submitToolOutput(String threadId, String runId,
                ThreadRunSubmitOutputRequest request) {
            var newRequest = request.withStream(Boolean.FALSE);
            return submitToolOutputPrimitive(threadId, runId, newRequest);
        }

        /**
         * Submit tool outputs to run without streaming and poll until a terminal status is reached.
         * 
         * @param threadId The ID of the thread to which this run belongs.
         * @param runId    The ID of the run that requires the tool output submission.
         * @param request  The submission request.
         * @return The modified run object matching the specified ID (no CompletableFuture).
         */
        default ThreadRun submitToolOutputAndPoll(String threadId, String runId,
                ThreadRunSubmitOutputRequest request) {
            var threadRun = submitToolOutput(threadId, runId, request).join();
            return Poller.<ThreadRun>builder()
                    .timeUnit(TimeUnit.SECONDS)
                    .timeValue(1)
                    .queryMethod(tr -> getOne(tr.getThreadId(), tr.getId()).join())
                    .whileMethod(tr -> tr.getStatus().equals(RunStatus.IN_PROGRESS))
                    .build()
                    .execute(threadRun);
        }

        /**
         * Submit tool outputs to run with streaming (don't call it directly).
         * 
         * @param threadId The ID of the thread to which this run belongs.
         * @param runId    The ID of the run that requires the tool output submission.
         * @param request  The submission request.
         * @return Stream of events that happen during the Run as server-sent events.
         */
        @POST("/{threadId}/runs/{runId}/submit_tool_outputs")
        @AssistantStreamEvent
        CompletableFuture<Stream<Event>> submitToolOutputStreamPrimitive(@Path("threadId") String threadId,
                @Path("runId") String runId, @Body ThreadRunSubmitOutputRequest request);

        /**
         * Submit tool outputs to run by making sure to use streaming mode.
         * 
         * @param threadId The ID of the thread to which this run belongs.
         * @param runId    The ID of the run that requires the tool output submission.
         * @param request  The submission request.
         * @return Stream of events that happen during the Run as server-sent events.
         */
        default CompletableFuture<Stream<Event>> submitToolOutputStream(String threadId, String runId,
                ThreadRunSubmitOutputRequest request) {
            var newRequest = request.withStream(Boolean.TRUE);
            return submitToolOutputStreamPrimitive(threadId, runId, newRequest);
        }

    }

    /**
     * Represents the steps (model and tool calls) taken during the run.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/run-steps">Thread Run Steps</a>
     */
    @Resource("/v1/threads/{threadId}/runs/{runId}/steps")
    @Header(name = Constant.OPENAI_BETA_HEADER, value = Constant.OPENAI_ASSISTANT_VERSION)
    interface ThreadRunSteps {

        /**
         * Returns a list of run steps belonging to a run.
         * 
         * @param threadId The ID of the thread the run and run steps belong to.
         * @param runId    The ID of the run the run steps belong to.
         * @param page     The page filter to narrow the list.
         * @return A list of run step objects.
         */
        @GET
        CompletableFuture<Page<ThreadRunStep>> getList(@Path("threadId") String threadId, @Path("runId") String runId,
                @Query PageRequest page);

        /**
         * Returns a list of run steps belonging to a run (first page).
         * 
         * @param threadId The ID of the thread the run and run steps belong to.
         * @param runId    The ID of the run the run steps belong to.
         * @return A list of run step objects.
         */
        default CompletableFuture<Page<ThreadRunStep>> getList(String threadId, String runId) {
            return getList(threadId, runId, PageRequest.builder().build());
        }

        /**
         * Retrieves a run step (don't call it directly).
         * 
         * @param threadId The ID of the thread to which the run and run step belongs.
         * @param runId    The ID of the run to which the run step belongs.
         * @param stepId   The ID of the run step to retrieve.
         * @param include  A list of additional fields to include in the response.
         * @return The run step object matching the specified ID.
         */
        @GET("/{stepId}")
        CompletableFuture<ThreadRunStep> getOnePrimitive(@Path("threadId") String threadId, @Path("runId") String runId,
                @Path("stepId") String stepId, @Query("include[]") ThreadRunStepQuery include);

        /**
         * Retrieves a run step.
         * 
         * @param threadId The ID of the thread to which the run and run step belongs.
         * @param runId    The ID of the run to which the run step belongs.
         * @param stepId   The ID of the run step to retrieve.
         * @return The run step object matching the specified ID.
         */
        @GET("/{stepId}")
        default CompletableFuture<ThreadRunStep> getOne(@Path("threadId") String threadId, @Path("runId") String runId,
                @Path("stepId") String stepId) {
            return getOnePrimitive(threadId, runId, stepId, null);
        }

        /**
         * Retrieves a run step including File Search result contents.
         * 
         * @param threadId The ID of the thread to which the run and run step belongs.
         * @param runId    The ID of the run to which the run step belongs.
         * @param stepId   The ID of the run step to retrieve.
         * @return The run step object matching the specified ID.
         */
        @GET("/{stepId}")
        default CompletableFuture<ThreadRunStep> getOneWithFileSearchResult(@Path("threadId") String threadId,
                @Path("runId") String runId, @Path("stepId") String stepId) {
            return getOnePrimitive(threadId, runId, stepId, ThreadRunStepQuery.FILE_SEARCH_RESULT);
        }

    }

    /**
     * Vector stores are used to store files for use by the file_search tool.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/vector-stores">Vector Stores</a>
     */
    @Resource("/v1/vector_stores")
    @Header(name = Constant.OPENAI_BETA_HEADER, value = Constant.OPENAI_ASSISTANT_VERSION)
    interface VectorStores {

        /**
         * Create a vector store.
         * 
         * @param request The creation request.
         * @return A vector store object.
         */
        @POST
        CompletableFuture<VectorStore> create(@Body VectorStoreRequest request);

        /**
         * Create a vector store without files.
         * 
         * @return A vector store object.
         */
        default CompletableFuture<VectorStore> create() {
            return create(VectorStoreRequest.builder().build());
        }

        /**
         * Create a vector store and poll until a terminal condition is reached.
         * 
         * @param request The creation request.
         * @return A vector store object (no CompletableFuture).
         */
        default VectorStore createAndPoll(VectorStoreRequest request) {
            var vectorStore = create(request).join();
            return Poller.<VectorStore>builder()
                    .timeUnit(TimeUnit.SECONDS)
                    .timeValue(1)
                    .queryMethod(vs -> getOne(vs.getId()).join())
                    .whileMethod(vs -> !vs.getStatus().equals(VectorStoreStatus.COMPLETED)
                            || vs.getFileCounts().getCompleted() != vs.getFileCounts().getTotal())
                    .build()
                    .execute(vectorStore);
        }

        /**
         * Returns a list of vector stores.
         * 
         * @param page The page filter to narrow the list.
         * @return A list of vector store objects.
         */
        @GET
        CompletableFuture<Page<VectorStore>> getList(@Query PageRequest page);

        default CompletableFuture<Page<VectorStore>> getList() {
            return getList(PageRequest.builder().build());
        }

        /**
         * Retrieves a vector store.
         * 
         * @param vectorStoreId The ID of the vector store to retrieve.
         * @return The vector store object matching the specified ID.
         */
        @GET("/{vectorStoreId}")
        CompletableFuture<VectorStore> getOne(@Path("vectorStoreId") String vectorStoreId);

        /**
         * Modifies a vector store.
         * 
         * @param vectorStoreId The ID of the vector store to modify.
         * @param request       The modification request.
         * @return The modified vector store object.
         */
        @POST("/{vectorStoreId}")
        CompletableFuture<VectorStore> modify(@Path("vectorStoreId") String vectorStoreId,
                @Body VectorStoreModifyRequest request);

        /**
         * Delete a vector store.
         * 
         * @param vectorStoreId The ID of the vector store to delete.
         * @return Deletion status.
         */
        @DELETE("/{vectorStoreId}")
        CompletableFuture<DeletedObject> delete(@Path("vectorStoreId") String vectorStoreId);

    }

    /**
     * Vector store files represent files inside a vector store.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/vector-stores-files">Vector Store
     *      Files</a>
     */
    @Resource("/v1/vector_stores/{vectorStoreId}/files")
    @Header(name = Constant.OPENAI_BETA_HEADER, value = Constant.OPENAI_ASSISTANT_VERSION)
    interface VectorStoreFiles {

        /**
         * Create a vector store file by attaching a File to a vector store.
         * 
         * @param vectorStoreId The ID of the vector store for which to create a File.
         * @param request       The creation request.
         * @return A vector store file object.
         */
        @POST
        CompletableFuture<VectorStoreFile> create(@Path("vectorStoreId") String vectorStoreId,
                @Body VectorStoreFileRequest request);

        /**
         * Create a vector store file by attaching a File to a vector store.
         * 
         * @param vectorStoreId The ID of the vector store for which to create a File.
         * @param fileId        A File ID that the vector store should use.
         * @return A vector store file object.
         */
        default CompletableFuture<VectorStoreFile> create(String vectorStoreId, String fileId) {
            return create(vectorStoreId, VectorStoreFileRequest.builder().fileId(fileId).build());
        }

        /**
         * Create a vector store file by attaching a File to a vector store and poll until a terminal
         * condition is reached.
         * 
         * @param vectorStoreId The ID of the vector store for which to create a File.
         * @param fileId        A File ID that the vector store should use.
         * @return A vector store file object (no CompletableFuture).
         */
        default VectorStoreFile createAndPoll(String vectorStoreId, String fileId) {
            var vectorStoreFile = create(vectorStoreId, fileId).join();
            return Poller.<VectorStoreFile>builder()
                    .timeUnit(TimeUnit.SECONDS)
                    .timeValue(1)
                    .queryMethod(vsf -> getOne(vsf.getVectorStoreId(), vsf.getId()).join())
                    .whileMethod(vsf -> !vsf.getStatus().equals(FileStatus.COMPLETED))
                    .build()
                    .execute(vectorStoreFile);
        }

        /**
         * Returns a list of vector store files.
         * 
         * @param vectorStoreId The ID of the vector store that the files belong to.
         * @param page          The page filter to narrow the list.
         * @param filter        Filter by file status.
         * @return A list of vector store file objects.
         */
        @GET
        CompletableFuture<Page<VectorStoreFile>> getList(@Path("vectorStoreId") String vectorStoreId,
                @Query PageRequest page, @Query("filter") FileStatus filter);

        /**
         * Returns a list of vector store files (first page).
         * 
         * @param vectorStoreId The ID of the vector store that the files belong to.
         * @return A list of vector store file objects.
         */
        default CompletableFuture<Page<VectorStoreFile>> getList(String vectorStoreId) {
            return getList(vectorStoreId, PageRequest.builder().build(), null);
        }

        /**
         * Retrieves a vector store file.
         * 
         * @param vectorStoreId The ID of the vector store that the file belongs to.
         * @param fileId        The ID of the file being retrieved.
         * @return The vector store file object.
         */
        @GET("/{fileId}")
        CompletableFuture<VectorStoreFile> getOne(@Path("vectorStoreId") String vectorStoreId,
                @Path("fileId") String fileId);

        /**
         * Delete a vector store file. To delete the file, use the delete file endpoint.
         * 
         * @param vectorStoreId The ID of the vector store that the file belongs to.
         * @param fileId        The ID of the file to delete.
         * @return Deletion status.
         */
        @DELETE("/{fileId}")
        CompletableFuture<DeletedObject> delete(@Path("vectorStoreId") String vectorStoreId,
                @Path("fileId") String fileId);

    }

    /**
     * Vector store file batches represent operations to add multiple files to a vector store.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/vector-stores-file-batches">Vector
     *      Store File Batches</a>
     */
    @Resource("/v1/vector_stores/{vectorStoreId}/file_batches")
    @Header(name = Constant.OPENAI_BETA_HEADER, value = Constant.OPENAI_ASSISTANT_VERSION)
    interface VectorStoreFileBatches {

        /**
         * Create a vector store file batch.
         * 
         * @param vectorStoreId The ID of the vector store for which to create a File Batch.
         * @param request       The creation request.
         * @return A vector store file batch object.
         */
        @POST
        CompletableFuture<VectorStoreFileBatch> create(@Path("vectorStoreId") String vectorStoreId,
                @Body VectorStoreFileBatchRequest request);

        /**
         * Create a vector store file batch.
         * 
         * @param vectorStoreId The ID of the vector store for which to create a File Batch.
         * @param fileIds       A list of File IDs that the vector store should use.
         * @return A vector store file batch object
         */
        default CompletableFuture<VectorStoreFileBatch> create(String vectorStoreId, List<String> fileIds) {
            return create(vectorStoreId, VectorStoreFileBatchRequest.builder().fileIds(fileIds).build());
        }

        /**
         * Create a vector store file batch and poll until a terminal condition is reached.
         * 
         * @param vectorStoreId The ID of the vector store for which to create a File Batch.
         * @param fileIds       A list of File IDs that the vector store should use.
         * @return A vector store file batch object (no CompletableFuture).
         */
        default VectorStoreFileBatch createAndPoll(String vectorStoreId, List<String> fileIds) {
            var vectorStoreFileBatch = create(vectorStoreId, fileIds).join();
            return Poller.<VectorStoreFileBatch>builder()
                    .timeUnit(TimeUnit.SECONDS)
                    .timeValue(1)
                    .queryMethod(vsfb -> getOne(vsfb.getVectorStoreId(), vsfb.getId()).join())
                    .whileMethod(vsfb -> !vsfb.getStatus().equals(FileStatus.COMPLETED))
                    .build()
                    .execute(vectorStoreFileBatch);
        }

        /**
         * Retrieves a vector store file batch.
         * 
         * @param vectorStoreId The ID of the vector store that the file batch belongs to.
         * @param batchId       The ID of the file batch being retrieved.
         * @return The vector store file batch object.
         */
        @GET("/{batchId}")
        CompletableFuture<VectorStoreFileBatch> getOne(@Path("vectorStoreId") String vectorStoreId,
                @Path("batchId") String batchId);

        /**
         * Cancel a vector store file batch.
         * 
         * @param vectorStoreId The ID of the vector store that the file batch belongs to.
         * @param batchId       The ID of the file batch to cancel.
         * @return The modified vector store file batch object.
         */
        @POST("/{batchId}/cancel")
        CompletableFuture<VectorStoreFileBatch> cancel(@Path("vectorStoreId") String vectorStoreId,
                @Path("batchId") String batchId);

        /**
         * Returns a list of vector store files in a batch.
         * 
         * @param vectorStoreId The ID of the vector store that the files belong to.
         * @param batchId       The ID of the file batch that the files belong to.
         * @param page          The page filter to narrow the list.
         * @param filter        Filter by file status.
         * @return A list of vector store file objects.
         */
        @GET("/{batchId}/files")
        CompletableFuture<Page<VectorStoreFile>> getFiles(@Path("vectorStoreId") String vectorStoreId,
                @Path("batchId") String batchId, @Query PageRequest page,
                @Query("filter") FileStatus filter);

        /**
         * Returns a list of vector store files in a batch (first page).
         * 
         * @param vectorStoreId The ID of the vector store that the files belong to.
         * @param batchId       The ID of the file batch that the files belong to.
         * @return A list of vector store file objects.
         */
        default CompletableFuture<Page<VectorStoreFile>> getFiles(String vectorStoreId, String batchId) {
            return getFiles(vectorStoreId, batchId, PageRequest.builder().build(), null);
        }

    }

}
