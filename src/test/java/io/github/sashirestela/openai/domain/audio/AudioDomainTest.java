package io.github.sashirestela.openai.domain.audio;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

public class AudioDomainTest {

  static HttpClient httpClient;
  static SimpleOpenAI openAI;
  static String fileName;

  @BeforeAll
  static void setup() {
    httpClient = mock(HttpClient.class);
    openAI = SimpleOpenAI.builder()
        .apiKey("apiKey")
        .httpClient(httpClient)
        .build();
    fileName = "src/demo/resources/hello_audio.mp3";
  }

  @Test
  void testAudiosTranscribe() throws IOException {
    DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/audios_transcribe.json");
    var audioRequest = AudioTranscribeRequest.builder()
        .file(Paths.get(fileName))
        .model("whisper-1")
        .prompt("It is a greeting")
        .responseFormat(AudioRespFmt.VERBOSE_JSON)
        .temperature(0.0)
        .language("en")
        .build();
    var audioResponse = openAI.audios().transcribe(audioRequest).join();
    System.out.println(audioResponse);
    assertNotNull(audioResponse);
  }

  @Test
  void testAudiosTranslate() throws IOException {
    DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/audios_translate.json");
    var audioRequest = AudioTranscribeRequest.builder()
        .file(Paths.get(fileName))
        .model("whisper-1")
        .prompt("It is a greeting")
        .responseFormat(AudioRespFmt.JSON)
        .temperature(0.0)
        .build();
    var audioResponse = openAI.audios().translate(audioRequest).join();
    System.out.println(audioResponse);
    assertNotNull(audioResponse);
  }

  @Test
  void testAudiosTranscribePlain() throws IOException {
    DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/audios_transcribe_plain.txt");
    var audioRequest = AudioTranscribeRequest.builder()
        .file(Paths.get(fileName))
        .model("whisper-1")
        .prompt("It is a greeting")
        .responseFormat(AudioRespFmt.TEXT)
        .temperature(0.0)
        .language("en")
        .build();
    var audioResponse = openAI.audios().transcribePlain(audioRequest).join();
    System.out.println(audioResponse);
    assertNotNull(audioResponse);
  }

  @Test
  void testAudiosTranslatePlain() throws IOException {
    DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/audios_translate_plain.txt");
    var audioRequest = AudioTranscribeRequest.builder()
        .file(Paths.get(fileName))
        .model("whisper-1")
        .prompt("It is a greeting")
        .responseFormat(AudioRespFmt.VTT)
        .temperature(0.0)
        .build();
    var audioResponse = openAI.audios().translatePlain(audioRequest).join();
    System.out.println(audioResponse);
    assertNotNull(audioResponse);
  }
}