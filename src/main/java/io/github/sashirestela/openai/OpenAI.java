package io.github.sashirestela.openai;

import static io.github.sashirestela.cleverclient.util.CommonUtil.isNullOrEmpty;

import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoiceType;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import io.github.sashirestela.cleverclient.annotation.Body;
import io.github.sashirestela.cleverclient.annotation.DELETE;
import io.github.sashirestela.cleverclient.annotation.GET;
import io.github.sashirestela.cleverclient.annotation.Header;
import io.github.sashirestela.cleverclient.annotation.Multipart;
import io.github.sashirestela.cleverclient.annotation.POST;
import io.github.sashirestela.cleverclient.annotation.Path;
import io.github.sashirestela.cleverclient.annotation.Query;
import io.github.sashirestela.cleverclient.annotation.Resource;
import io.github.sashirestela.openai.domain.OpenAIDeletedResponse;
import io.github.sashirestela.openai.domain.OpenAIGeneric;
import io.github.sashirestela.openai.domain.PageRequest;
import io.github.sashirestela.openai.domain.Page;
import io.github.sashirestela.openai.domain.assistant.Assistant;
import io.github.sashirestela.openai.domain.assistant.AssistantFile;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.FilePath;
import io.github.sashirestela.openai.domain.assistant.Thread;
import io.github.sashirestela.openai.domain.assistant.ThreadCreateAndRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessage;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageFile;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunStep;
import io.github.sashirestela.openai.domain.assistant.ToolOutput;
import io.github.sashirestela.openai.domain.assistant.ToolOutputSubmission;
import io.github.sashirestela.openai.domain.audio.AudioRespFmt;
import io.github.sashirestela.openai.domain.audio.AudioResponse;
import io.github.sashirestela.openai.domain.audio.AudioSpeechRequest;
import io.github.sashirestela.openai.domain.audio.AudioTranscribeRequest;
import io.github.sashirestela.openai.domain.audio.AudioTranslateRequest;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.completion.CompletionRequest;
import io.github.sashirestela.openai.domain.completion.CompletionResponse;
import io.github.sashirestela.openai.domain.embedding.EmbeddingBase64Response;
import io.github.sashirestela.openai.domain.embedding.EmbeddingFloatResponse;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;
import io.github.sashirestela.openai.domain.embedding.EncodingFormat;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileResponse;
import io.github.sashirestela.openai.domain.finetuning.FineTuningEvent;
import io.github.sashirestela.openai.domain.finetuning.FineTuningRequest;
import io.github.sashirestela.openai.domain.finetuning.FineTuningResponse;
import io.github.sashirestela.openai.domain.image.ImageEditsRequest;
import io.github.sashirestela.openai.domain.image.ImageRequest;
import io.github.sashirestela.openai.domain.image.ImageResponse;
import io.github.sashirestela.openai.domain.image.ImageVariationsRequest;
import io.github.sashirestela.openai.domain.model.ModelResponse;
import io.github.sashirestela.openai.domain.moderation.ModerationRequest;
import io.github.sashirestela.openai.domain.moderation.ModerationResponse;

/**
 * The OpenAI API can be applied to virtually any task that requires
 * understanding or generating natural language and code. The OpenAI API can
 * also be used to generate and edit images or convert speech into text.
 * 
 * @see <a href="https://platform.openai.com/docs/api-reference">OpenAI API</a>
 */
public interface OpenAI {

    /**
     * Turn audio into text (speech to text).
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/audio">OpenAI
     *      Audio</a>
     */
    @Resource("/v1/audio")
    interface Audios {

        /**
         * Generates audio from the input text.
         * 
         * @param speechRequest Includes the text to generate audio for, and audio file
         *                      format among others.
         * @return The audio file content.
         */
        @POST("/speech")
        CompletableFuture<InputStream> speak(@Body AudioSpeechRequest speechRequest);

        /**
         * Transcribes audio into the input language. Response as object.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: json,
         *                     verbose_json. Includes the audio file.
         * @return Transcription as an object.
         */
        default CompletableFuture<AudioResponse> transcribe(AudioTranscribeRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioRespFmt.JSON, "transcribe");
            var request = audioRequest.withResponseFormat(responseFormat);
            return __transcribe(request);
        }

        @Multipart
        @POST("/transcriptions")
        CompletableFuture<AudioResponse> __transcribe(@Body AudioTranscribeRequest audioRequest);

        /**
         * Translates audio into English. Response as object.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: json,
         *                     verbose_json. Includes the audio file.
         * @return Translation as an object.
         */
        default CompletableFuture<AudioResponse> translate(AudioTranslateRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioRespFmt.JSON, "transcribe");
            var request = audioRequest.withResponseFormat(responseFormat);
            return __translate(request);
        }

        @Multipart
        @POST("/translations")
        CompletableFuture<AudioResponse> __translate(@Body AudioTranslateRequest audioRequest);

        /**
         * Transcribes audio into the input language. Response as plain text.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: text, srt, vtt.
         *                     Includes the audio file.
         * @return Transcription as plain text.
         */
        default CompletableFuture<String> transcribePlain(AudioTranscribeRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioRespFmt.TEXT, "transcribe");
            var request = audioRequest.withResponseFormat(responseFormat);
            return __transcribePlain(request);
        }

        @Multipart
        @POST("/transcriptions")
        CompletableFuture<String> __transcribePlain(@Body AudioTranscribeRequest audioRequest);

        /**
         * Translates audio into English. Response as plain text.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: text, srt, vtt.
         *                     Includes the audio file.
         * @return Translation as plain text.
         */
        default CompletableFuture<String> translatePlain(AudioTranslateRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioRespFmt.TEXT, "transcribe");
            var request = audioRequest.withResponseFormat(responseFormat);
            return __translatePlain(request);
        }

        @Multipart
        @POST("/translations")
        CompletableFuture<String> __translatePlain(@Body AudioTranslateRequest audioRequest);

        private AudioRespFmt getResponseFormat(AudioRespFmt currValue, AudioRespFmt orDefault, String methodName) {
            final var jsonEnumSet = EnumSet.of(AudioRespFmt.JSON, AudioRespFmt.VERBOSE_JSON);
            final var textEnumSet = EnumSet.complementOf(jsonEnumSet);

            var isText = textEnumSet.contains(orDefault);
            var requestedFormat = currValue;
            if (requestedFormat != null) {
                if (isText != textEnumSet.contains(requestedFormat)) {
                    throw new SimpleUncheckedException("Unexpected responseFormat for the method {0}.",
                            methodName, null);
                }
            } else {
                requestedFormat = orDefault;
            }
            return requestedFormat;
        }
    }

    /**
     * Given a list of messages comprising a conversation, the model will return a
     * response.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/chat">OpenAI
     *      Chat</a>
     */
    @Resource("/v1/chat/completions")
    interface ChatCompletions {

        public static ChatRequest updateRequest(ChatRequest chatRequest, Boolean useStream) {
            var toolChoice = chatRequest.getToolChoice();
            if (!isNullOrEmpty(chatRequest.getTools()) && toolChoice == null) {
                toolChoice = ChatToolChoiceType.AUTO;
            }
            return chatRequest
                .withStream(useStream)
                .withToolChoice(toolChoice);
        }

        /**
         * Creates a model response for the given chat conversation. Blocking mode.
         * 
         * @param chatRequest Includes a list of messages comprising the conversation.
         *                    Its 'stream' attribute is setted to false automatically.
         * @return Response is delivered as a full text when is ready.
         */
        default CompletableFuture<ChatResponse> create(@Body ChatRequest chatRequest) {
            var request = updateRequest(chatRequest, Boolean.FALSE);
            return __create(request);
        }

        @POST
        CompletableFuture<ChatResponse> __create(@Body ChatRequest chatRequest);

        /**
         * Creates a model response for the given chat conversation. Streaming Mode.
         * 
         * @param chatRequest Includes a list of messages comprising the conversation.
         *                    Its 'stream' attribute is setted to true automatically.
         * @return Response is delivered as a continues flow of tokens.
         */
        default CompletableFuture<Stream<ChatResponse>> createStream(@Body ChatRequest chatRequest) {
            var request = updateRequest(chatRequest, Boolean.TRUE);
            return __createStream(request);
        }

        @POST
        CompletableFuture<Stream<ChatResponse>> __createStream(@Body ChatRequest chatRequest);

    }

    /**
     * Given a prompt, the model will return one or more predicted completions. It
     * is recommended for most users to use the Chat Completion.
     * 
     * @see <a href=
     *      "https://platform.openai.com/docs/api-reference/completions">OpenAI
     *      Completion</a>
     */
    @Resource("/v1/completions")
    interface Completions {

        /**
         * Creates a completion for the provided prompt and parameters. Blocking mode.
         * 
         * @param completionRequest Includes the prompt(s) to generate completions for.
         *                          Its 'stream' attribute is setted to false
         *                          automatically.
         * @return Response is delivered as a full text when is ready.
         */
        default CompletableFuture<CompletionResponse> create(@Body CompletionRequest completionRequest) {
            var request = completionRequest.withStream(Boolean.FALSE);
            return __create(request);
        }

        @POST
        CompletableFuture<CompletionResponse> __create(@Body CompletionRequest completionRequest);

        /**
         * Creates a completion for the provided prompt and parameters. Streaming mode.
         * 
         * @param completionRequest Includes the prompt(s) to generate completions for.
         *                          Its 'stream' attribute is setted to true
         *                          automatically.
         * @return Response is delivered as a continuous flow of tokens.
         */
        default CompletableFuture<Stream<CompletionResponse>> createStream(@Body CompletionRequest completionRequest) {
            var request = completionRequest.withStream(Boolean.TRUE);
            return __createStream(request);
        }

        @POST
        CompletableFuture<Stream<CompletionResponse>> __createStream(@Body CompletionRequest completionRequest);

    }

    /**
     * Get a vector representation of a given input that can be easily consumed by
     * machine learning models and algorithms.
     * 
     * @see <a href=
     *      "https://platform.openai.com/docs/api-reference/embeddings">OpenAI
     *      Embedding</a>
     */
    @Resource("/v1/embeddings")
    interface Embeddings {

        /**
         * Creates an embedding vector representing the input text.
         * 
         * @param embeddingRequest The input text to embed and the model to use.
         * @return Represents an embedding vector in array of float format.
         */
        default CompletableFuture<EmbeddingFloatResponse> create(@Body EmbeddingRequest embeddingRequest) {
            var request = embeddingRequest.withEncodingFormat(EncodingFormat.FLOAT);
            return __create(request);
        }

        @POST
        CompletableFuture<EmbeddingFloatResponse> __create(@Body EmbeddingRequest embeddingRequest);

        /**
         * Creates an embedding vector representing the input text.
         * 
         * @param embeddingRequest The input text to embed and the model to use.
         * @return Represents an embedding vector in base64 format.
         */
        default CompletableFuture<EmbeddingBase64Response> createBase64(@Body EmbeddingRequest embeddingRequest) {
            var request = embeddingRequest.withEncodingFormat(EncodingFormat.BASE64);
            return __createBase64(request);
        }

        @POST
        CompletableFuture<EmbeddingBase64Response> __createBase64(@Body EmbeddingRequest embeddingRequest);

    }

    /**
     * Files are used to upload documents that can be used with features like
     * fine-tuning.
     * 
     * @see <a href=
     *      "https://platform.openai.com/docs/api-reference/files">OpenAI
     *      Files</a>
     */
    @Resource("/v1/files")
    interface Files {

        /**
         * Upload a file that contains document(s) to be used across various
         * endpoints/features. Currently *.jsonl files are supported only.
         * 
         * @param fileRequest Includes the file to be uploaded.
         * @return Represents a document that has been uploaded.
         */
        @Multipart
        @POST
        CompletableFuture<FileResponse> create(@Body FileRequest fileRequest);

        /**
         * Returns a list of files that belong to the user's organization.
         * 
         * @param purpose Only return files with the given purpose.
         * @return List of files.
         */
        default CompletableFuture<List<FileResponse>> getList(String purpose) {
            return __getList(purpose).thenApply(OpenAIGeneric::getData);
        }

        @GET
        CompletableFuture<OpenAIGeneric<FileResponse>> __getList(@Query("purpose") String purpose);

        /**
         * Returns information about a specific file.
         * 
         * @param fileId The id of the file to use for this request.
         * @return Specific file.
         */
        @GET("/{fileId}")
        CompletableFuture<FileResponse> getOne(@Path("fileId") String fileId);

        /**
         * Returns a file content.
         * 
         * @param fileId The id of the file to use for this request.
         * @return Content of specific file.
         */
        @GET("/{fileId}/content")
        CompletableFuture<String> getContent(@Path("fileId") String fileId);

        /**
         * Returns a file content as a stream.
         *
         * @param fileId The id of the file to use for this request.
         * @return Content of specific file.
         */
        @GET("/{fileId}/content")
        CompletableFuture<InputStream> getContentInputStream(@Path("fileId") String fileId);

        /**
         * Delete a file.
         * 
         * @param fileId The id of the file to use for this request.
         * @return Deletion status.
         */
        @DELETE("/{fileId}")
        CompletableFuture<OpenAIDeletedResponse> delete(@Path("fileId") String fileId);

    }

    /**
     * Manage fine-tuning jobs to tailor a model to your specific training data.
     * 
     * @see <a href=
     *      "https://platform.openai.com/docs/api-reference/fine-tuning">OpenAI
     *      Fine-Tuning</a>
     */
    @Resource("/v1/fine_tuning/jobs")
    interface FineTunings {

        /**
         * Creates a job that fine-tunes a specified model from a given dataset.
         * 
         * @param fineTuningRequest Includes the trainig file in format jsonl and the
         *                          base model to fine-tune.
         * @return Response includes details of the enqueued job including job status
         *         and the name of the fine-tuned models once complete.
         */
        @POST
        CompletableFuture<FineTuningResponse> create(@Body FineTuningRequest fineTuningRequest);

        /**
         * List your organization's fine-tuning jobs.
         * 
         * @param limit Number of fine-tuning jobs to retrieve.
         * @param after Identifier for the last job from the previous pagination
         *              request.
         * @return A list of paginated fine-tuning job objects.
         */
        default CompletableFuture<List<FineTuningResponse>> getList(Integer limit, String after) {
            return __getList(limit, after).thenApply(OpenAIGeneric::getData);
        }

        @GET
        CompletableFuture<OpenAIGeneric<FineTuningResponse>> __getList(@Query("limit") Integer limit,
                @Query("after") String after);

        /**
         * Get info about a fine-tuning job.
         * 
         * @param fineTuningId The id of the fine-tuning job.
         * @return The fine-tuning object with the given id.
         */
        @GET("/{fineTuningId}")
        CompletableFuture<FineTuningResponse> getOne(@Path("fineTuningId") String fineTuningId);

        /**
         * Get status updates for a fine-tuning job.
         * 
         * @param fineTuningId The id of the fine-tuning job to get events for.
         * @param limit        Number of fine-tuning jobs to retrieve.
         * @param after        Identifier for the last job from the previous pagination
         *                     request.
         * @return A list of fine-tuning event objects.
         */
        default CompletableFuture<List<FineTuningEvent>> getEvents(String fineTuningId, Integer limit, String after) {
            return __getEvents(fineTuningId, limit, after).thenApply(OpenAIGeneric::getData);
        }

        @GET("/{fineTuningId}/events")
        CompletableFuture<OpenAIGeneric<FineTuningEvent>> __getEvents(@Path("fineTuningId") String fineTuningId,
                @Query("limit") Integer limit, @Query("after") String after);

        /**
         * Immediately cancel a fine-tune job.
         * 
         * @param fineTuningId The id of the fine-tuning job to cancel.
         * @return The cancelled fine-tuning object.
         */
        @POST("/{fineTuningId}/cancel")
        CompletableFuture<FineTuningResponse> cancel(@Path("fineTuningId") String fineTuningId);

    }

    /**
     * Given a prompt and/or an input image, the model will generate a new image.
     * 
     * @see <a href=
     *      "https://platform.openai.com/docs/api-reference/images">OpenAI
     *      Image</a>
     */
    @Resource("/v1/images")
    interface Images {

        /**
         * Creates an image given a prompt.
         * 
         * @param imageRequest A text description of the desired image(s) and other
         *                     parameters such as number, size or responseFormat.
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<ImageResponse>> create(ImageRequest imageRequest) {
            return __create(imageRequest).thenApply(OpenAIGeneric::getData);
        }

        @POST("/generations")
        CompletableFuture<OpenAIGeneric<ImageResponse>> __create(@Body ImageRequest imageRequest);

        /**
         * Creates an edited or extended image given an original image and a prompt.
         * 
         * @param imageRequest Includes the image file to edit and a text description of
         *                     the desired image(s).
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<ImageResponse>> createEdits(ImageEditsRequest imageRequest) {
            return __createEdits(imageRequest).thenApply(OpenAIGeneric::getData);
        }

        @Multipart
        @POST("/edits")
        CompletableFuture<OpenAIGeneric<ImageResponse>> __createEdits(@Body ImageEditsRequest imageRequest);

        /**
         * Creates a variation of a given image.
         * 
         * @param imageRequest Includes the image file to use as the basis for the
         *                     variation(s).
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<ImageResponse>> createVariations(ImageVariationsRequest imageRequest) {
            return __createVariations(imageRequest).thenApply(OpenAIGeneric::getData);
        }

        @Multipart
        @POST("/variations")
        CompletableFuture<OpenAIGeneric<ImageResponse>> __createVariations(@Body ImageVariationsRequest imageRequest);
    }

    /**
     * List and describe the various models available in the API.
     * 
     * @see <a href=
     *      "https://platform.openai.com/docs/api-reference/models">OpenAI
     *      Model</a>
     */
    @Resource("/v1/models")
    interface Models {

        /**
         * Lists the currently available models, and provides basic information about
         * each one such as the owner and availability.
         * 
         * @return A list of model objects.
         */
        default CompletableFuture<List<ModelResponse>> getList() {
            return __getList().thenApply(OpenAIGeneric::getData);
        }

        @GET
        CompletableFuture<OpenAIGeneric<ModelResponse>> __getList();

        /**
         * Retrieves a model instance, providing basic information about the model such
         * as the owner and permissioning.
         * 
         * @param modelId The id of the model to use for this request.
         * @return The model object matching the specified id.
         */
        @GET("/{modelId}")
        CompletableFuture<ModelResponse> getOne(@Path("modelId") String modelId);

        /**
         * Delete a fine tuned model.
         * 
         * @param modelId The id of the dine tuned model to use for this request.
         * @return Deletion status.
         */
        @DELETE("/{modelId}")
        CompletableFuture<OpenAIDeletedResponse> delete(@Path("modelId") String modelId);

    }

    /**
     * Given a input text, outputs if the model classifies it as violating OpenAI's
     * content policy.
     * 
     * @see <a href=
     *      "https://platform.openai.com/docs/api-reference/moderations">OpenAI
     *      Moderation</a>
     */
    @Resource("/v1/moderations")
    interface Moderations {

        /**
         * Classifies if text violates OpenAI's Content Policy.
         * 
         * @param moderationRequest Includes the input text to classify and the model to
         *                          be used.
         * @return Response including a list of moderation objects.
         */
        @POST
        CompletableFuture<ModerationResponse> create(@Body ModerationRequest moderationRequest);

    }

    /**
     * Build assistants that can call models and use tools to perform tasks.
     *
     * @see <a href=
     *      "https://platform.openai.com/docs/api-reference/assistants">OpenAI
     *      Assistants</a>
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
         * @param fileId      A File ID (with purpose="assistants") that the assistant
         *                    should use.
         * @return the created assistant file object.
         */
        default CompletableFuture<AssistantFile> createFile(String assistantId, String fileId) {
            return createFile(assistantId, FilePath.of(fileId));
        }

        /**
         * Create an assistant file by attaching a File to an assistant.
         *
         * @param assistantId The ID of the assistant for which to create a File.
         * @param file        A File ID (with purpose="assistants") that the assistant
         *                    should use.
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
     * @see <a href="https://platform.openai.com/docs/api-reference/threads">OpenAI
     *      Threads</a>
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
         * @param threadRequest The thread request.
         * @return the created thread object
         */
        @POST("/{threadId}")
        CompletableFuture<Thread> modify(@Path("threadId") String threadId, @Body ThreadRequest threadRequest);

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
                @Path("messageId") String messageId, @Body ThreadMessageRequest request);

        /**
         * Returns a list of messages for a given thread (first page only).
         *
         * @param threadId The ID of the thread the messages belong to.
         * @return The list of message objects.
         */
        default CompletableFuture<Page<ThreadMessage>> getMessageList(String threadId) {
            return getMessageList(threadId, PageRequest.builder().build());
        }

        /**
         * Returns a list of messages for a given thread.
         *
         * @param threadId The ID of the thread the messages belong to.
         * @param page     The requested result page.
         * @return The list of message objects.
         */
        @GET("/{threadId}/messages")
        CompletableFuture<Page<ThreadMessage>> getMessageList(@Path("threadId") String threadId,
                @Query PageRequest page);

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
            return createRun(threadId, ThreadRunRequest.builder()
                    .assistantId(assistantId)
                    .build());
        }

        /**
         * Create a run.
         *
         * @param threadId The ID of the thread to run.
         * @param request  The requested run.
         * @return the queued run object
         */
        @POST("/{threadId}/runs")
        CompletableFuture<ThreadRun> createRun(@Path("threadId") String threadId, @Body ThreadRunRequest request);

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
         * @return The modified run object matching the specified ID.
         */
        @POST("/{threadId}/runs/{runId}")
        CompletableFuture<ThreadRun> modifyRun(@Path("threadId") String threadId, @Path("runId") String runId,
                @Body ThreadRunRequest request);

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
         * Submit tool outputs to run
         *
         * @param threadId    The ID of the thread to which this run belongs.
         * @param runId       The ID of the run that requires the tool output
         *                    submission.
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
         * Submit tool outputs to run
         *
         * @param threadId    The ID of the thread to which this run belongs.
         * @param runId       The ID of the run that requires the tool output
         *                    submission.
         * @param toolOutputs The tool output submission.
         * @return The modified run object matching the specified ID.
         */
        @POST("/{threadId}/runs/{runId}/submit_tool_outputs")
        CompletableFuture<ThreadRun> submitToolOutputs(@Path("threadId") String threadId, @Path("runId") String runId,
                @Body ToolOutputSubmission toolOutputs);

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
        @POST("/runs")
        CompletableFuture<ThreadRun> createThreadAndRun(@Body ThreadCreateAndRunRequest request);

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