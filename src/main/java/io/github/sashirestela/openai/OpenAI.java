package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.annotation.Body;
import io.github.sashirestela.cleverclient.annotation.DELETE;
import io.github.sashirestela.cleverclient.annotation.GET;
import io.github.sashirestela.cleverclient.annotation.Multipart;
import io.github.sashirestela.cleverclient.annotation.POST;
import io.github.sashirestela.cleverclient.annotation.Path;
import io.github.sashirestela.cleverclient.annotation.Query;
import io.github.sashirestela.cleverclient.annotation.Resource;
import io.github.sashirestela.openai.domain.Deleted;
import io.github.sashirestela.openai.domain.Generic;
import io.github.sashirestela.openai.domain.Page;
import io.github.sashirestela.openai.domain.audio.AudioResponseFormat;
import io.github.sashirestela.openai.domain.audio.SpeechRequest;
import io.github.sashirestela.openai.domain.audio.Transcription;
import io.github.sashirestela.openai.domain.audio.TranscriptionRequest;
import io.github.sashirestela.openai.domain.audio.TranslationRequest;
import io.github.sashirestela.openai.domain.batch.Batch;
import io.github.sashirestela.openai.domain.batch.BatchRequest;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.completion.Completion;
import io.github.sashirestela.openai.domain.completion.CompletionRequest;
import io.github.sashirestela.openai.domain.embedding.Embedding;
import io.github.sashirestela.openai.domain.embedding.EmbeddingBase64;
import io.github.sashirestela.openai.domain.embedding.EmbeddingFloat;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest.EncodingFormat;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileResponse;
import io.github.sashirestela.openai.domain.finetuning.FineTuning;
import io.github.sashirestela.openai.domain.finetuning.FineTuningCheckpoint;
import io.github.sashirestela.openai.domain.finetuning.FineTuningEvent;
import io.github.sashirestela.openai.domain.finetuning.FineTuningRequest;
import io.github.sashirestela.openai.domain.image.Image;
import io.github.sashirestela.openai.domain.image.ImageEditsRequest;
import io.github.sashirestela.openai.domain.image.ImageRequest;
import io.github.sashirestela.openai.domain.image.ImageVariationsRequest;
import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.domain.moderation.Moderation;
import io.github.sashirestela.openai.domain.moderation.ModerationRequest;
import io.github.sashirestela.openai.tool.ToolChoiceOption;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static io.github.sashirestela.cleverclient.util.CommonUtil.isNullOrEmpty;

/**
 * The OpenAI API can be applied to virtually any task that requires understanding or generating
 * natural language and code. The OpenAI API can also be used to generate and edit images or convert
 * speech into text.
 * 
 * @see <a href="https://platform.openai.com/docs/api-reference">OpenAI API</a>
 */
public interface OpenAI {

    /**
     * Turn audio into text (speech to text).
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/audio">OpenAI Audio</a>
     */
    @Resource("/v1/audio")
    interface Audios {

        /**
         * Generates audio from the input text.
         * 
         * @param speechRequest Includes the text to generate audio for, and audio file format among others.
         * @return The audio file content.
         */
        @POST("/speech")
        CompletableFuture<InputStream> speak(@Body SpeechRequest speechRequest);

        /**
         * Transcribes audio into the input language. Response as object.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: json, verbose_json. Includes the
         *                     audio file.
         * @return Transcription as an object.
         */
        default CompletableFuture<Transcription> transcribe(TranscriptionRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioResponseFormat.JSON,
                    "transcribe");
            var request = audioRequest.withResponseFormat(responseFormat);
            return transcribeRoot(request);
        }

        @Multipart
        @POST("/transcriptions")
        CompletableFuture<Transcription> transcribeRoot(@Body TranscriptionRequest audioRequest);

        /**
         * Translates audio into English. Response as object.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: json, verbose_json. Includes the
         *                     audio file.
         * @return Translation as an object.
         */
        default CompletableFuture<Transcription> translate(TranslationRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioResponseFormat.JSON,
                    "translate");
            var request = audioRequest.withResponseFormat(responseFormat);
            return translateRoot(request);
        }

        @Multipart
        @POST("/translations")
        CompletableFuture<Transcription> translateRoot(@Body TranslationRequest audioRequest);

        /**
         * Transcribes audio into the input language. Response as plain text.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: text, srt, vtt. Includes the audio
         *                     file.
         * @return Transcription as plain text.
         */
        default CompletableFuture<String> transcribePlain(TranscriptionRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioResponseFormat.TEXT,
                    "transcribe");
            var request = audioRequest.withResponseFormat(responseFormat);
            return transcribePlainRoot(request);
        }

        @Multipart
        @POST("/transcriptions")
        CompletableFuture<String> transcribePlainRoot(@Body TranscriptionRequest audioRequest);

        /**
         * Translates audio into English. Response as plain text.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: text, srt, vtt. Includes the audio
         *                     file.
         * @return Translation as plain text.
         */
        default CompletableFuture<String> translatePlain(TranslationRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioResponseFormat.TEXT,
                    "translate");
            var request = audioRequest.withResponseFormat(responseFormat);
            return translatePlainRoot(request);
        }

        @Multipart
        @POST("/translations")
        CompletableFuture<String> translatePlainRoot(@Body TranslationRequest audioRequest);

    }

    /**
     * Create large batches of API requests to run asynchronously.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/batch">OpenAI Batch</a>
     */
    @Resource("/v1/batches")
    interface Batches {

        /**
         * Creates and executes a batch from an uploaded file of requests.
         * 
         * @param batchRequest It includes the uploade file reference.
         * @return The batch object.
         */
        @POST
        CompletableFuture<Batch> create(@Body BatchRequest batchRequest);

        /**
         * Retrieves a batch.
         * 
         * @param batchId The id of the batch to retrieve.
         * @return The Batch object matching the specified id.
         */
        @GET("/{batchId}")
        CompletableFuture<Batch> getOne(@Path("batchId") String batchId);

        /**
         * Cancels an in-progress batch.
         * 
         * @param batchId The id of the batch to cancel.
         * @return The Batch object matching the specified id.
         */
        @POST("/{batchId}/cancel")
        CompletableFuture<Batch> cancel(@Path("batchId") String batchId);

        /**
         * List your organization's batches.
         * 
         * @param after A cursor for use in pagination. It is an object ID that defines your place in the
         *              list. For instance, if you make a list request and receive 100 objects, ending with
         *              obj_foo, your subsequent call can include after=obj_foo in order to fetch the next
         *              page of the list.
         * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100,
         *              and the default is 20.
         * @return A list of paginated Batch objects.
         */
        @GET
        CompletableFuture<Page<Batch>> getList(@Query("after") String after, @Query("limit") Integer limit);

    }

    /**
     * Given a list of messages comprising a conversation, the model will return a response.
     * 
     * @see <a href="https://platform.openai.com/docs/api-reference/chat">OpenAI Chat</a>
     */
    @Resource("/v1/chat/completions")
    interface ChatCompletions {

        /**
         * Creates a model response for the given chat conversation. Blocking mode.
         * 
         * @param chatRequest Includes a list of messages comprising the conversation. Its 'stream'
         *                    attribute is setted to false automatically.
         * @return Response is delivered as a full text when is ready.
         */
        default CompletableFuture<Chat> create(@Body ChatRequest chatRequest) {
            var request = updateRequest(chatRequest, Boolean.FALSE);
            return createRoot(request);
        }

        @POST
        CompletableFuture<Chat> createRoot(@Body ChatRequest chatRequest);

        /**
         * Creates a model response for the given chat conversation. Streaming Mode.
         * 
         * @param chatRequest Includes a list of messages comprising the conversation. Its 'stream'
         *                    attribute is setted to true automatically.
         * @return Response is delivered as a continues flow of tokens.
         */
        default CompletableFuture<Stream<Chat>> createStream(@Body ChatRequest chatRequest) {
            var request = updateRequest(chatRequest, Boolean.TRUE);
            return createStreamRoot(request);
        }

        @POST
        CompletableFuture<Stream<Chat>> createStreamRoot(@Body ChatRequest chatRequest);

    }

    /**
     * Given a prompt, the model will return one or more predicted completions. It is recommended for
     * most users to use the Chat Completion.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/completions">OpenAI Completion</a>
     */
    @Resource("/v1/completions")
    interface Completions {

        /**
         * Creates a completion for the provided prompt and parameters. Blocking mode.
         * 
         * @param completionRequest Includes the prompt(s) to generate completions for. Its 'stream'
         *                          attribute is setted to false automatically.
         * @return Response is delivered as a full text when is ready.
         */
        default CompletableFuture<Completion> create(@Body CompletionRequest completionRequest) {
            var request = completionRequest.withStream(Boolean.FALSE);
            return createRoot(request);
        }

        @POST
        CompletableFuture<Completion> createRoot(@Body CompletionRequest completionRequest);

        /**
         * Creates a completion for the provided prompt and parameters. Streaming mode.
         * 
         * @param completionRequest Includes the prompt(s) to generate completions for. Its 'stream'
         *                          attribute is setted to true automatically.
         * @return Response is delivered as a continuous flow of tokens.
         */
        default CompletableFuture<Stream<Completion>> createStream(@Body CompletionRequest completionRequest) {
            var request = completionRequest.withStream(Boolean.TRUE);
            return createStreamRoot(request);
        }

        @POST
        CompletableFuture<Stream<Completion>> createStreamRoot(@Body CompletionRequest completionRequest);

    }

    /**
     * Get a vector representation of a given input that can be easily consumed by machine learning
     * models and algorithms.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/embeddings">OpenAI Embedding</a>
     */
    @Resource("/v1/embeddings")
    interface Embeddings {

        /**
         * Creates an embedding vector representing the input text.
         * 
         * @param embeddingRequest The input text to embed and the model to use.
         * @return Represents an embedding vector in array of float format.
         */
        default CompletableFuture<Embedding<EmbeddingFloat>> create(@Body EmbeddingRequest embeddingRequest) {
            var request = embeddingRequest.withEncodingFormat(EncodingFormat.FLOAT);
            return createRoot(request);
        }

        @POST
        CompletableFuture<Embedding<EmbeddingFloat>> createRoot(@Body EmbeddingRequest embeddingRequest);

        /**
         * Creates an embedding vector representing the input text.
         * 
         * @param embeddingRequest The input text to embed and the model to use.
         * @return Represents an embedding vector in base64 format.
         */
        default CompletableFuture<Embedding<EmbeddingBase64>> createBase64(@Body EmbeddingRequest embeddingRequest) {
            var request = embeddingRequest.withEncodingFormat(EncodingFormat.BASE64);
            return createBase64Root(request);
        }

        @POST
        CompletableFuture<Embedding<EmbeddingBase64>> createBase64Root(@Body EmbeddingRequest embeddingRequest);

    }

    /**
     * Files are used to upload documents that can be used with features like fine-tuning.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/files">OpenAI Files</a>
     */
    @Resource("/v1/files")
    interface Files {

        /**
         * Upload a file that contains document(s) to be used across various endpoints/features. Currently
         * *.jsonl files are supported only.
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
            return getListRoot(purpose).thenApply(Generic::getData);
        }

        @GET
        CompletableFuture<Generic<FileResponse>> getListRoot(@Query("purpose") String purpose);

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
        CompletableFuture<Deleted> delete(@Path("fileId") String fileId);

    }

    /**
     * Manage fine-tuning jobs to tailor a model to your specific training data.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/fine-tuning">OpenAI Fine-Tuning</a>
     */
    @Resource("/v1/fine_tuning/jobs")
    interface FineTunings {

        /**
         * Creates a job that fine-tunes a specified model from a given dataset.
         * 
         * @param fineTuningRequest Includes the trainig file in format jsonl and the base model to
         *                          fine-tune.
         * @return Response includes details of the enqueued job including job status and the name of the
         *         fine-tuned models once complete.
         */
        @POST
        CompletableFuture<FineTuning> create(@Body FineTuningRequest fineTuningRequest);

        /**
         * List your organization's fine-tuning jobs.
         * 
         * @param limit Number of fine-tuning jobs to retrieve.
         * @param after Identifier for the last job from the previous pagination request.
         * @return A list of paginated fine-tuning job objects.
         */
        default CompletableFuture<List<FineTuning>> getList(Integer limit, String after) {
            return getListRoot(limit, after).thenApply(Generic::getData);
        }

        @GET
        CompletableFuture<Generic<FineTuning>> getListRoot(@Query("limit") Integer limit,
                @Query("after") String after);

        /**
         * Get info about a fine-tuning job.
         * 
         * @param fineTuningId The id of the fine-tuning job.
         * @return The fine-tuning object with the given id.
         */
        @GET("/{fineTuningId}")
        CompletableFuture<FineTuning> getOne(@Path("fineTuningId") String fineTuningId);

        /**
         * Get status updates for a fine-tuning job.
         * 
         * @param fineTuningId The id of the fine-tuning job to get events for.
         * @param limit        Number of fine-tuning jobs to retrieve.
         * @param after        Identifier for the last job from the previous pagination request.
         * @return A list of fine-tuning event objects.
         */
        default CompletableFuture<List<FineTuningEvent>> getEvents(String fineTuningId, Integer limit, String after) {
            return getEventsRoot(fineTuningId, limit, after).thenApply(Generic::getData);
        }

        @GET("/{fineTuningId}/events")
        CompletableFuture<Generic<FineTuningEvent>> getEventsRoot(@Path("fineTuningId") String fineTuningId,
                @Query("limit") Integer limit, @Query("after") String after);

        /**
         * List checkpoints for a fine-tuning job.
         * 
         * @param fineTuningId The id of the fine-tuning job to get checkpoints for.
         * @param limit        Number of fine-tuning jobs to retrieve.
         * @param after        Identifier for the last job from the previous pagination request.
         * @return A list of fine-tuning checkpoint objects.
         */
        default CompletableFuture<List<FineTuningCheckpoint>> getCheckpoints(String fineTuningId, Integer limit,
                String after) {
            return getCheckpointsRoot(fineTuningId, limit, after).thenApply(Generic::getData);
        }

        @GET("/{fineTuningId}/checkpoints")
        CompletableFuture<Generic<FineTuningCheckpoint>> getCheckpointsRoot(@Path("fineTuningId") String fineTuningId,
                @Query("limit") Integer limit, @Query("after") String after);

        /**
         * Immediately cancel a fine-tune job.
         * 
         * @param fineTuningId The id of the fine-tuning job to cancel.
         * @return The cancelled fine-tuning object.
         */
        @POST("/{fineTuningId}/cancel")
        CompletableFuture<FineTuning> cancel(@Path("fineTuningId") String fineTuningId);

    }

    /**
     * Given a prompt and/or an input image, the model will generate a new image.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/images">OpenAI Image</a>
     */
    @Resource("/v1/images")
    interface Images {

        /**
         * Creates an image given a prompt.
         * 
         * @param imageRequest A text description of the desired image(s) and other parameters such as
         *                     number, size or responseFormat.
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<Image>> create(ImageRequest imageRequest) {
            return createRoot(imageRequest).thenApply(Generic::getData);
        }

        @POST("/generations")
        CompletableFuture<Generic<Image>> createRoot(@Body ImageRequest imageRequest);

        /**
         * Creates an edited or extended image given an original image and a prompt.
         * 
         * @param imageRequest Includes the image file to edit and a text description of the desired
         *                     image(s).
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<Image>> createEdits(ImageEditsRequest imageRequest) {
            return createEditsRoot(imageRequest).thenApply(Generic::getData);
        }

        @Multipart
        @POST("/edits")
        CompletableFuture<Generic<Image>> createEditsRoot(@Body ImageEditsRequest imageRequest);

        /**
         * Creates a variation of a given image.
         * 
         * @param imageRequest Includes the image file to use as the basis for the variation(s).
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<Image>> createVariations(ImageVariationsRequest imageRequest) {
            return createVariationsRoot(imageRequest).thenApply(Generic::getData);
        }

        @Multipart
        @POST("/variations")
        CompletableFuture<Generic<Image>> createVariationsRoot(@Body ImageVariationsRequest imageRequest);

    }

    /**
     * List and describe the various models available in the API.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/models">OpenAI Model</a>
     */
    @Resource("/v1/models")
    interface Models {

        /**
         * Lists the currently available models, and provides basic information about each one such as the
         * owner and availability.
         * 
         * @return A list of model objects.
         */
        default CompletableFuture<List<Model>> getList() {
            return getListRoot().thenApply(Generic::getData);
        }

        @GET
        CompletableFuture<Generic<Model>> getListRoot();

        /**
         * Retrieves a model instance, providing basic information about the model such as the owner and
         * permissioning.
         * 
         * @param modelId The id of the model to use for this request.
         * @return The model object matching the specified id.
         */
        @GET("/{modelId}")
        CompletableFuture<Model> getOne(@Path("modelId") String modelId);

        /**
         * Delete a fine tuned model.
         * 
         * @param modelId The id of the dine tuned model to use for this request.
         * @return Deletion status.
         */
        @DELETE("/{modelId}")
        CompletableFuture<Deleted> delete(@Path("modelId") String modelId);

    }

    /**
     * Given a input text, outputs if the model classifies it as violating OpenAI's content policy.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/moderations">OpenAI Moderation</a>
     */
    @Resource("/v1/moderations")
    interface Moderations {

        /**
         * Classifies if text violates OpenAI's Content Policy.
         * 
         * @param moderationRequest Includes the input text to classify and the model to be used.
         * @return Response including a list of moderation objects.
         */
        @POST
        CompletableFuture<Moderation> create(@Body ModerationRequest moderationRequest);

    }

    static AudioResponseFormat getResponseFormat(AudioResponseFormat currValue, AudioResponseFormat orDefault,
            String methodName) {
        final var jsonEnumSet = EnumSet.of(AudioResponseFormat.JSON, AudioResponseFormat.VERBOSE_JSON);
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

    static ChatRequest updateRequest(ChatRequest chatRequest, Boolean useStream) {
        var updatedChatRequest = chatRequest.withStream(useStream);
        if (!isNullOrEmpty(chatRequest.getTools()) && chatRequest.getToolChoice() == null) {
            updatedChatRequest = updatedChatRequest.withToolChoice(ToolChoiceOption.AUTO);
        }
        return updatedChatRequest;
    }

}
