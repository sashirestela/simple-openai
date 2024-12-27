package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.annotation.Body;
import io.github.sashirestela.cleverclient.annotation.DELETE;
import io.github.sashirestela.cleverclient.annotation.GET;
import io.github.sashirestela.cleverclient.annotation.Multipart;
import io.github.sashirestela.cleverclient.annotation.POST;
import io.github.sashirestela.cleverclient.annotation.Path;
import io.github.sashirestela.cleverclient.annotation.Query;
import io.github.sashirestela.cleverclient.annotation.Resource;
import io.github.sashirestela.openai.common.DeletedObject;
import io.github.sashirestela.openai.common.Generic;
import io.github.sashirestela.openai.common.Page;
import io.github.sashirestela.openai.common.StreamOptions;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
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
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
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
import io.github.sashirestela.openai.domain.realtime.RealtimeSession;
import io.github.sashirestela.openai.domain.realtime.RealtimeSessionToken;
import io.github.sashirestela.openai.domain.upload.Upload;
import io.github.sashirestela.openai.domain.upload.UploadCompleteRequest;
import io.github.sashirestela.openai.domain.upload.UploadPart;
import io.github.sashirestela.openai.domain.upload.UploadPartRequest;
import io.github.sashirestela.openai.domain.upload.UploadRequest;
import io.github.sashirestela.openai.exception.SimpleOpenAIException;

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
         * Transcribes audio into the input language as object (don't call it directly).
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: json, verbose_json. Includes the
         *                     audio file.
         * @return Transcription as an object.
         */
        @Multipart
        @POST("/transcriptions")
        CompletableFuture<Transcription> transcribePrimitive(@Body TranscriptionRequest audioRequest);

        /**
         * Translates audio into English as object (don't call it directly).
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: json, verbose_json. Includes the
         *                     audio file.
         * @return Translation as an object.
         */
        @Multipart
        @POST("/translations")
        CompletableFuture<Transcription> translatePrimitive(@Body TranslationRequest audioRequest);

        /**
         * Transcribes audio into the input language as plain text (don't call it directly).
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: text, srt, vtt. Includes the audio
         *                     file.
         * @return Transcription as plain text.
         */
        @Multipart
        @POST("/transcriptions")
        CompletableFuture<String> transcribePlainPrimitive(@Body TranscriptionRequest audioRequest);

        /**
         * Translates audio into English as plain text (don't call it directly).
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: text, srt, vtt. Includes the audio
         *                     file.
         * @return Translation as plain text.
         */
        @Multipart
        @POST("/translations")
        CompletableFuture<String> translatePlainPrimitive(@Body TranslationRequest audioRequest);

        /**
         * Transcribes audio into the input language as object.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: json, verbose_json. Includes the
         *                     audio file.
         * @return Transcription as an object.
         */
        default CompletableFuture<Transcription> transcribe(TranscriptionRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioResponseFormat.JSON,
                    "transcribe");
            var request = audioRequest.withResponseFormat(responseFormat);
            return transcribePrimitive(request);
        }

        /**
         * Translates audio into English as object.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: json, verbose_json. Includes the
         *                     audio file.
         * @return Translation as an object.
         */
        default CompletableFuture<Transcription> translate(TranslationRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioResponseFormat.JSON,
                    "translate");
            var request = audioRequest.withResponseFormat(responseFormat);
            return translatePrimitive(request);
        }

        /**
         * Transcribes audio into the input language as plain text.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: text, srt, vtt. Includes the audio
         *                     file.
         * @return Transcription as plain text.
         */
        default CompletableFuture<String> transcribePlain(TranscriptionRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioResponseFormat.TEXT,
                    "transcribe");
            var request = audioRequest.withResponseFormat(responseFormat);
            return transcribePlainPrimitive(request);
        }

        /**
         * Translates audio into English as plain text.
         * 
         * @param audioRequest Its 'responseFormat' attribute should be: text, srt, vtt. Includes the audio
         *                     file.
         * @return Translation as plain text.
         */
        default CompletableFuture<String> translatePlain(TranslationRequest audioRequest) {
            var responseFormat = getResponseFormat(audioRequest.getResponseFormat(), AudioResponseFormat.TEXT,
                    "translate");
            var request = audioRequest.withResponseFormat(responseFormat);
            return translatePlainPrimitive(request);
        }

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
         * Creates a model response for the given chat conversation without streaming (don't call it
         * directly).
         * 
         * @param chatRequest Includes a list of messages comprising the conversation.
         * @return Response is delivered as a full text when is ready.
         */
        @POST
        CompletableFuture<Chat> createPrimitive(@Body ChatRequest chatRequest);

        /**
         * Creates a model response for the given chat conversation with streaming (don't call it directly).
         * 
         * @param chatRequest Includes a list of messages comprising the conversation.
         * @return Response is delivered as a continuous stream of tokens.
         */
        @POST
        CompletableFuture<Stream<Chat>> createStreamPrimitive(@Body ChatRequest chatRequest);

        /**
         * Creates a model response for the given chat conversation without streaming.
         * 
         * @param chatRequest Includes a list of messages comprising the conversation. Its 'stream'
         *                    attribute is setted to false automatically.
         * @return Response is delivered as a full text when is ready.
         */
        default CompletableFuture<Chat> create(@Body ChatRequest chatRequest) {
            var request = updateRequest(chatRequest, Boolean.FALSE);
            return createPrimitive(request);
        }

        /**
         * Creates a model response for the given chat conversation with streaming.
         * 
         * @param chatRequest Includes a list of messages comprising the conversation. Its 'stream'
         *                    attribute is setted to true automatically.
         * @return Response is delivered as a continuous stream of tokens.
         */
        default CompletableFuture<Stream<Chat>> createStream(@Body ChatRequest chatRequest) {
            var request = updateRequest(chatRequest, Boolean.TRUE);
            return createStreamPrimitive(request);
        }

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
         * Creates a completion for the provided prompt and parameters without streaming (don't call it
         * directly).
         * 
         * @param completionRequest Includes the prompt(s) to generate completions for.
         * @return Response is delivered as a full text when is ready.
         */
        @POST
        CompletableFuture<Completion> createPrimitive(@Body CompletionRequest completionRequest);

        /**
         * Creates a completion for the provided prompt and parameters with streaming (don't call it
         * directly).
         * 
         * @param completionRequest Includes the prompt(s) to generate completions for.
         * @return Response is delivered as a continuous flow of tokens.
         */
        @POST
        CompletableFuture<Stream<Completion>> createStreamPrimitive(@Body CompletionRequest completionRequest);

        /**
         * Creates a completion for the provided prompt and parameters without streaming.
         * 
         * @param completionRequest Includes the prompt(s) to generate completions for. Its 'stream'
         *                          attribute is setted to false automatically.
         * @return Response is delivered as a full text when is ready.
         */
        default CompletableFuture<Completion> create(@Body CompletionRequest completionRequest) {
            var request = completionRequest.withStream(Boolean.FALSE);
            return createPrimitive(request);
        }

        /**
         * Creates a completion for the provided prompt and parameters with streaming.
         * 
         * @param completionRequest Includes the prompt(s) to generate completions for. Its 'stream'
         *                          attribute is setted to true automatically.
         * @return Response is delivered as a continuous flow of tokens.
         */
        default CompletableFuture<Stream<Completion>> createStream(@Body CompletionRequest completionRequest) {
            var request = completionRequest.withStream(Boolean.TRUE).withStreamOptions(StreamOptions.of(Boolean.TRUE));
            return createStreamPrimitive(request);
        }

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
         * Creates an embedding vector representing the input text (don't call it directly).
         * 
         * @param embeddingRequest The input text to embed and the model to use.
         * @return Represents an embedding vector in array of float format.
         */
        @POST
        CompletableFuture<Embedding<EmbeddingFloat>> createPrimitive(@Body EmbeddingRequest embeddingRequest);

        /**
         * Creates an embedding vector representing the input text (don't call it directly).
         * 
         * @param embeddingRequest The input text to embed and the model to use.
         * @return Represents an embedding vector in base64 format.
         */
        @POST
        CompletableFuture<Embedding<EmbeddingBase64>> createBase64Primitive(@Body EmbeddingRequest embeddingRequest);

        /**
         * Creates an embedding vector representing the input text.
         * 
         * @param embeddingRequest The input text to embed and the model to use.
         * @return Represents an embedding vector in array of float format.
         */
        default CompletableFuture<Embedding<EmbeddingFloat>> create(@Body EmbeddingRequest embeddingRequest) {
            var request = embeddingRequest.withEncodingFormat(EncodingFormat.FLOAT);
            return createPrimitive(request);
        }

        /**
         * Creates an embedding vector representing the input text.
         * 
         * @param embeddingRequest The input text to embed and the model to use.
         * @return Represents an embedding vector in base64 format.
         */
        default CompletableFuture<Embedding<EmbeddingBase64>> createBase64(@Body EmbeddingRequest embeddingRequest) {
            var request = embeddingRequest.withEncodingFormat(EncodingFormat.BASE64);
            return createBase64Primitive(request);
        }

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
         * Returns a Generic of files that belong to the user's organization (don't call it directly).
         * 
         * @param purpose Only return files with the given purpose.
         * @return Generic object of files.
         */
        @GET
        CompletableFuture<Generic<FileResponse>> getListPrimitive(@Query("purpose") PurposeType purpose);

        /**
         * Returns a list of files that belong to the user's organization.
         * 
         * @param purpose Only return files with the given purpose.
         * @return List of files.
         */
        default CompletableFuture<List<FileResponse>> getList(PurposeType purpose) {
            return getListPrimitive(purpose).thenApply(Generic::getData);
        }

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
        CompletableFuture<DeletedObject> delete(@Path("fileId") String fileId);

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
         * Generic of your organization's fine-tuning jobs (don't call it directly).
         * 
         * @param limit Number of fine-tuning jobs to retrieve.
         * @param after Identifier for the last job from the previous pagination request.
         * @return Generic object of fine-tunings.
         */
        @GET
        CompletableFuture<Generic<FineTuning>> getListPrimitive(@Query("limit") Integer limit,
                @Query("after") String after);

        /**
         * List your organization's fine-tuning jobs.
         * 
         * @param limit Number of fine-tuning jobs to retrieve.
         * @param after Identifier for the last job from the previous pagination request.
         * @return A list of paginated fine-tuning job objects.
         */
        default CompletableFuture<List<FineTuning>> getList(Integer limit, String after) {
            return getListPrimitive(limit, after).thenApply(Generic::getData);
        }

        /**
         * Get info about a fine-tuning job.
         * 
         * @param fineTuningId The id of the fine-tuning job.
         * @return The fine-tuning object with the given id.
         */
        @GET("/{fineTuningId}")
        CompletableFuture<FineTuning> getOne(@Path("fineTuningId") String fineTuningId);

        /**
         * Get status updates for a fine-tuning job as Generic object (don't call it directly).
         * 
         * @param fineTuningId The id of the fine-tuning job to get events for.
         * @param limit        Number of fine-tuning jobs to retrieve.
         * @param after        Identifier for the last job from the previous pagination request.
         * @return Generic of fine-tuning event object.
         */
        @GET("/{fineTuningId}/events")
        CompletableFuture<Generic<FineTuningEvent>> getEventsPrimitive(@Path("fineTuningId") String fineTuningId,
                @Query("limit") Integer limit, @Query("after") String after);

        /**
         * Get status updates for a fine-tuning job.
         * 
         * @param fineTuningId The id of the fine-tuning job to get events for.
         * @param limit        Number of fine-tuning jobs to retrieve.
         * @param after        Identifier for the last job from the previous pagination request.
         * @return A list of fine-tuning event objects.
         */
        default CompletableFuture<List<FineTuningEvent>> getEvents(String fineTuningId, Integer limit, String after) {
            return getEventsPrimitive(fineTuningId, limit, after).thenApply(Generic::getData);
        }

        /**
         * Generic of checkpoints for a fine-tuning job (don't call it directly).
         * 
         * @param fineTuningId The id of the fine-tuning job to get checkpoints for.
         * @param limit        Number of fine-tuning jobs to retrieve.
         * @param after        Identifier for the last job from the previous pagination request.
         * @return Generic of fine-tuning checkpoint object.
         */
        @GET("/{fineTuningId}/checkpoints")
        CompletableFuture<Generic<FineTuningCheckpoint>> getCheckpointsPrimitive(
                @Path("fineTuningId") String fineTuningId,
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
            return getCheckpointsPrimitive(fineTuningId, limit, after).thenApply(Generic::getData);
        }

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
         * Creates a Genric object of image given a prompt (don't call it directly).
         * 
         * @param imageRequest A text description of the desired image(s) and other parameters such as
         *                     number, size or responseFormat.
         * @return Returns a Generic of image objects (the url or the binary content).
         */
        @POST("/generations")
        CompletableFuture<Generic<Image>> createPrimitive(@Body ImageRequest imageRequest);

        /**
         * Creates a Generic of edited or extended image given an original image and a prompt (don't call it
         * directly).
         * 
         * @param imageRequest Includes the image file to edit and a text description of the desired
         *                     image(s).
         * @return Returns a Generic of image objects (the url or the binary content).
         */
        @Multipart
        @POST("/edits")
        CompletableFuture<Generic<Image>> createEditsPrimitive(@Body ImageEditsRequest imageRequest);

        /**
         * Creates a Generic of variation of a given image (don't call it directly).
         * 
         * @param imageRequest Includes the image file to use as the basis for the variation(s).
         * @return Returns a Generic of image objects (the url or the binary content).
         */
        @Multipart
        @POST("/variations")
        CompletableFuture<Generic<Image>> createVariationsPrimitive(@Body ImageVariationsRequest imageRequest);

        /**
         * Creates an image given a prompt.
         * 
         * @param imageRequest A text description of the desired image(s) and other parameters such as
         *                     number, size or responseFormat.
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<Image>> create(ImageRequest imageRequest) {
            return createPrimitive(imageRequest).thenApply(Generic::getData);
        }

        /**
         * Creates an edited or extended image given an original image and a prompt.
         * 
         * @param imageRequest Includes the image file to edit and a text description of the desired
         *                     image(s).
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<Image>> createEdits(ImageEditsRequest imageRequest) {
            return createEditsPrimitive(imageRequest).thenApply(Generic::getData);
        }

        /**
         * Creates a variation of a given image.
         * 
         * @param imageRequest Includes the image file to use as the basis for the variation(s).
         * @return Returns a list of image objects (the url or the binary content).
         */
        default CompletableFuture<List<Image>> createVariations(ImageVariationsRequest imageRequest) {
            return createVariationsPrimitive(imageRequest).thenApply(Generic::getData);
        }

    }

    /**
     * List and describe the various models available in the API.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/models">OpenAI Model</a>
     */
    @Resource("/v1/models")
    interface Models {

        /**
         * Generic of the currently available models, and provides basic information about each one such as
         * the owner and availability (don't call it directly).
         * 
         * @return A Generic of model objects.
         */
        @GET
        CompletableFuture<Generic<Model>> getListPrimitive();

        /**
         * Lists the currently available models, and provides basic information about each one such as the
         * owner and availability.
         * 
         * @return A list of model objects.
         */
        default CompletableFuture<List<Model>> getList() {
            return getListPrimitive().thenApply(Generic::getData);
        }

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
        CompletableFuture<DeletedObject> delete(@Path("modelId") String modelId);

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

    /**
     * Generate ephemeral session tokens for use in client-side applications.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/realtime-sessions">OpenAI
     *      Session-Tokens</a>
     */
    @Resource("/v1/realtime/sessions")
    interface SessionTokens {

        /**
         * Create an ephemeral API token for use in client-side applications with the Realtime API.
         * 
         * @param sessionRequest A Realtime session configuration including the model.
         * @return A new Realtime session configuration, with an ephermeral token.
         */
        @POST
        CompletableFuture<RealtimeSessionToken> create(@Body RealtimeSession sessionRequest);

    }

    /**
     * Allows you to upload large files in multiple parts.
     * 
     * @see <a href= "https://platform.openai.com/docs/api-reference/uploads">OpenAI Upload</a>
     */
    @Resource("/v1/uploads")
    interface Uploads {

        /**
         * Creates an intermediate Upload object that you can add Parts to.
         * 
         * @param uploadRequest The creation request.
         * @return The Upload object with status pending.
         */
        @POST
        CompletableFuture<Upload> create(@Body UploadRequest uploadRequest);

        /**
         * Adds a Part to an Upload object. A Part represents a chunk of bytes from the file you are trying
         * to upload.
         * 
         * @param uploadId          The ID of the Upload.
         * @param uploadPartRequest The chunk of bytes for this Part.
         * @return The upload Part object.
         */
        @Multipart
        @POST("/{uploadId}/parts")
        CompletableFuture<UploadPart> addPart(@Path("uploadId") String uploadId,
                @Body UploadPartRequest uploadPartRequest);

        /**
         * Completes the Upload.
         * 
         * @param uploadId              The ID of the Upload.
         * @param uploadCompleteRequest The request including the ordered list of Part IDs.
         * @return The Upload object with status completed.
         */
        @POST("/{uploadId}/complete")
        CompletableFuture<Upload> complete(@Path("uploadId") String uploadId,
                @Body UploadCompleteRequest uploadCompleteRequest);

        /**
         * Cancels the Upload. No Parts may be added after an Upload is cancelled.
         * 
         * @param uploadId The ID of the Upload.
         * @return The Upload object with status cancelled.
         */
        @POST("/{uploadId}/cancel")
        CompletableFuture<Upload> cancel(@Path("uploadId") String uploadId);

    }

    static AudioResponseFormat getResponseFormat(AudioResponseFormat currValue, AudioResponseFormat orDefault,
            String methodName) {
        final var jsonEnumSet = EnumSet.of(AudioResponseFormat.JSON, AudioResponseFormat.VERBOSE_JSON);
        final var textEnumSet = EnumSet.complementOf(jsonEnumSet);

        var isText = textEnumSet.contains(orDefault);
        var requestedFormat = currValue;
        if (requestedFormat != null) {
            if (isText != textEnumSet.contains(requestedFormat)) {
                throw new SimpleOpenAIException("Unexpected responseFormat for the method {0}.",
                        methodName, null);
            }
        } else {
            requestedFormat = orDefault;
        }
        return requestedFormat;
    }

    static ChatRequest updateRequest(ChatRequest chatRequest, Boolean useStream) {
        var updatedChatRequest = chatRequest.withStream(useStream);
        if (Boolean.TRUE.equals(useStream)) {
            updatedChatRequest = updatedChatRequest.withStreamOptions(StreamOptions.of(useStream));
        }
        if (!isNullOrEmpty(chatRequest.getTools()) && chatRequest.getToolChoice() == null) {
            updatedChatRequest = updatedChatRequest.withToolChoice(ToolChoiceOption.AUTO);
        }
        return updatedChatRequest;
    }

}
