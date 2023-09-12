package io.github.sashirestela.openai.domain.audio;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;

@SuppressWarnings("unchecked")
public class AudioDomainTest {

  static HttpClient httpClient;
  static SimpleOpenAI openAI;
  static HttpResponse<String> httpResponse;
  static String fileName;

  @BeforeAll
  static void setup() {
    httpClient = mock(HttpClient.class);
    openAI = SimpleOpenAI.builder()
        .apiKey("apiKey")
        .httpClient(httpClient)
        .build();
    httpResponse = mock(HttpResponse.class);
    fileName = "src/demo/resources/hello_audio.mp3";
  }

  @Test
  void testAudiosTranscribe() throws IOException {
    String jsonResponse = Files.readAllLines(Paths.get("src/test/resources/audios_transcribe.json")).get(0);
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(jsonResponse);

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
    String jsonResponse = Files.readAllLines(Paths.get("src/test/resources/audios_translate.json")).get(0);
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(jsonResponse);

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
    String jsonResponse = Files.readAllLines(Paths.get("src/test/resources/audios_transcribe_plain.txt")).get(0);
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(jsonResponse);

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
    String jsonResponse = Files.readAllLines(Paths.get("src/test/resources/audios_translate_plain.txt")).get(0);
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(jsonResponse);

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
