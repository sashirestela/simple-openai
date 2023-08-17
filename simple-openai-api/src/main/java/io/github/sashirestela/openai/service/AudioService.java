package io.github.sashirestela.openai.service;

import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.audio.AudioResponse;
import io.github.sashirestela.openai.domain.audio.TranscriptionRequest;
import io.github.sashirestela.openai.domain.audio.TranslationRequest;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.Multipart;
import io.github.sashirestela.openai.http.annotation.POST;

public interface AudioService {

  @Multipart
  @POST("/v1/audio/transcriptions")
  CompletableFuture<AudioResponse> callAudioTranscription(@Body TranscriptionRequest audioRequest);

  @Multipart
  @POST("/v1/audio/translations")
  CompletableFuture<AudioResponse> callAudioTranslation(@Body TranslationRequest audioRequest);

}