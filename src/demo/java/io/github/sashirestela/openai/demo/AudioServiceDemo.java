package io.github.sashirestela.openai.demo;

import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.audio.AudioResponse;
import io.github.sashirestela.openai.domain.audio.AudioRespFmt;
import io.github.sashirestela.openai.domain.audio.AudioTranscribeRequest;
import io.github.sashirestela.openai.domain.audio.AudioTranslateRequest;

public class AudioServiceDemo extends AbstractDemo {

  private String fileName;

  public AudioServiceDemo() {
    this.fileName = "src/demo/resources/hello_audio.mp3";
  }

  public void demoCallAudioTranscription() {
    AudioTranscribeRequest audioRequest = AudioTranscribeRequest.builder()
        .file(Paths.get(fileName))
        .model("whisper-1")
        .responseFormat(AudioRespFmt.VERBOSE_JSON)
        .build();
    CompletableFuture<AudioResponse> futureAudio = openAI.audios().transcribe(audioRequest);
    AudioResponse audioResponse = futureAudio.join();
    System.out.println(audioResponse);
  }

  public void demoCallAudioTranslation() {
    AudioTranslateRequest audioRequest = AudioTranslateRequest.builder()
        .file(Paths.get(fileName))
        .model("whisper-1")
        .build();
    CompletableFuture<AudioResponse> futureAudio = openAI.audios().translate(audioRequest);
    AudioResponse audioResponse = futureAudio.join();
    System.out.println(audioResponse);
  }

  public void demoCallAudioTranscriptionPlain() {
    AudioTranscribeRequest audioRequest = AudioTranscribeRequest.builder()
        .file(Paths.get(fileName))
        .model("whisper-1")
        .build();
    CompletableFuture<String> futureAudio = openAI.audios().transcribePlain(audioRequest);
    String audioResponse = futureAudio.join();
    System.out.println(audioResponse);
  }

  public void demoCallAudioTranslationPlain() {
    AudioTranslateRequest audioRequest = AudioTranslateRequest.builder()
        .file(Paths.get(fileName))
        .model("whisper-1")
        .responseFormat(AudioRespFmt.VTT)
        .build();
    CompletableFuture<String> futureAudio = openAI.audios().translatePlain(audioRequest);
    String audioResponse = futureAudio.join();
    System.out.println(audioResponse);
  }

  public static void main(String[] args) {
    AudioServiceDemo demo = new AudioServiceDemo();

    demo.addTitleAction("Call Audio Transcription", () -> demo.demoCallAudioTranscription());
    demo.addTitleAction("Call Audio Translation", () -> demo.demoCallAudioTranslation());
    demo.addTitleAction("Call Audio Transcription Plain", () -> demo.demoCallAudioTranscriptionPlain());
    demo.addTitleAction("Call Audio Translation Plain", () -> demo.demoCallAudioTranslationPlain());

    demo.run();
  }
}