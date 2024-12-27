package io.github.sashirestela.openai.domain.audio;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.exception.SimpleOpenAIException;
import io.github.sashirestela.openai.test.captors.CapturedValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AudioFilterTest {

    SimpleOpenAI openAI;
    HttpClient httpClient = mock(HttpClient.class);

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        when(httpClient.sendAsync(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(mock(HttpResponse.class)));
    }

    @Test
    void shouldSendRespFmtTextWhenCallingTranscribePlainMethodOfAudioServiceWithoutRespFmt() {
        var audioRequest = TranscriptionRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .build();
        openAI.audios().transcribePlain(audioRequest);

        var httpRequest = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).sendAsync(httpRequest.capture(), any());
        assertContainsText(AudioResponseFormat.TEXT, httpRequest);
    }

    @Test
    void shouldKeepRespFmtWhenCallingTranscribePlainMethodOfAudioServiceWithTextishRespFmt() {
        var audioRequest = TranscriptionRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .responseFormat(AudioResponseFormat.SRT)
                .build();
        openAI.audios().transcribePlain(audioRequest);
        assertEquals(AudioResponseFormat.SRT, audioRequest.getResponseFormat());
    }

    @Test
    void shouldThrowExceptionWhenCallingTranscribePlainMethodOfAudioServiceWithJsonishRespFmt() {
        var audioRequest = TranscriptionRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .responseFormat(AudioResponseFormat.JSON)
                .build();
        var audioService = openAI.audios();
        Exception exception = assertThrows(SimpleOpenAIException.class,
                () -> audioService.transcribePlain(audioRequest));
        assertTrue(exception.getMessage().contains("Unexpected responseFormat for the method"));
    }

    @Test
    void shouldSetRespFmtToJsonWhenCallingTranscribeMethodOfAudioServiceWithoutRespFmt() {
        var audioRequest = TranscriptionRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .build();
        openAI.audios().transcribe(audioRequest);

        var httpRequest = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).sendAsync(httpRequest.capture(), any());
        assertContainsText(AudioResponseFormat.JSON, httpRequest);
    }

    static void assertContainsText(AudioResponseFormat responseFormat, ArgumentCaptor<HttpRequest> httpRequest) {
        String requestBody = CapturedValues.getRequestBodyAsString(httpRequest);
        assertTrue(requestBody.contains(responseFormat.name().toLowerCase()),
                "Should contain " + requestBody + " in HttpRequest");
    }

    @Test
    void shouldKeepRespFmtWhenCallingTranscribeMethodOfAudioServiceWithJsonishRespFmt() {
        var audioRequest = TranscriptionRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .responseFormat(AudioResponseFormat.VERBOSE_JSON)
                .build();
        openAI.audios().transcribe(audioRequest);
        assertEquals(AudioResponseFormat.VERBOSE_JSON, audioRequest.getResponseFormat());
    }

    @Test
    void shouldThrowExceptionWhenCallingTranscribeMethodOfAudioServiceWithTextishRespFmt() {
        var audioRequest = TranscriptionRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .responseFormat(AudioResponseFormat.TEXT)
                .build();
        var audioService = openAI.audios();
        Exception exception = assertThrows(SimpleOpenAIException.class,
                () -> audioService.transcribe(audioRequest));
        assertTrue(exception.getMessage().contains("Unexpected responseFormat for the method"));
    }

}
