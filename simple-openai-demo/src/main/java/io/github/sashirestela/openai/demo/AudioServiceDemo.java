package io.github.sashirestela.openai.demo;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.audio.AudioResponse;
import io.github.sashirestela.openai.domain.audio.TranscriptFormat;
import io.github.sashirestela.openai.domain.audio.TranscriptionRequest;
import io.github.sashirestela.openai.domain.audio.TranslationRequest;
import io.github.sashirestela.openai.service.AudioService;

public class AudioServiceDemo extends AbstractDemo {

  private AudioService audioService;
  private String fileName;

  public AudioServiceDemo(String fileName) {
    audioService = openAIApi.createAudioService();
    this.fileName = fileName;
  }

  public void demoCallAudioTranscription() {
    TranscriptionRequest audioRequest = TranscriptionRequest.builder()
        .file(new File(fileName))
        .model("whisper-1")
        .responseFormat(TranscriptFormat.VERBOSE_JSON)
        .build();
    CompletableFuture<AudioResponse> futureAudio = audioService.callAudioTranscription(audioRequest);
    AudioResponse audioResponse = futureAudio.join();
    System.out.println(audioResponse.getText());
  }

  public void demoCallAudioTranslation() {
    TranslationRequest audioRequest = TranslationRequest.builder()
        .file(new File(fileName))
        .model("whisper-1")
        .responseFormat(TranscriptFormat.VERBOSE_JSON)
        .build();
    CompletableFuture<AudioResponse> futureAudio = audioService.callAudioTranslation(audioRequest);
    AudioResponse audioResponse = futureAudio.join();
    System.out.println(audioResponse.getText());
  }

  public static void main(String[] args) {
    String fileName = args[0];
    AudioServiceDemo demo = new AudioServiceDemo(fileName);

    demo.addTitleAction("Call Audio Transcription", () -> demo.demoCallAudioTranscription());
    demo.addTitleAction("Call Audio Translation", () -> demo.demoCallAudioTranslation());

    demo.run();
  }
}
