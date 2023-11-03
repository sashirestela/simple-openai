package io.github.sashirestela.openai.domain.audio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.SimpleUncheckedException;

class AudioFilterTest {

    static SimpleOpenAI openAI;
    static HttpClient httpClient = mock(HttpClient.class);

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void setup() {
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        when(httpClient.sendAsync(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(mock(HttpResponse.class)));
    }

    @Test
    void shouldSetRespFmtToTextWhenCallingTranscribePlainMethodOfAudioServiceWithoutRespFmt() {
        var audioRequest = AudioTranscribeRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .build();
        openAI.audios().transcribePlain(audioRequest);
        assertEquals(AudioRespFmt.TEXT, audioRequest.getResponseFormat());
    }

    @Test
    void shouldKeepRespFmtWhenCallingTranscribePlainMethodOfAudioServiceWithTextishRespFmt() {
        var audioRequest = AudioTranscribeRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .responseFormat(AudioRespFmt.SRT)
                .build();
        openAI.audios().transcribePlain(audioRequest);
        assertEquals(AudioRespFmt.SRT, audioRequest.getResponseFormat());
    }

    @Test
    void shouldThrowExceptionWhenCallingTranscribePlainMethodOfAudioServiceWithJsonishRespFmt() {
        var audioRequest = AudioTranscribeRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .responseFormat(AudioRespFmt.JSON)
                .build();
        Exception exception = assertThrows(SimpleUncheckedException.class,
                () -> openAI.audios().transcribePlain(audioRequest));
        assertTrue(exception.getMessage().contains("Unexpected responseFormat for the method"));
    }

    @Test
    void shouldSetRespFmtToJsonWhenCallingTranscribeMethodOfAudioServiceWithoutRespFmt() {
        var audioRequest = AudioTranscribeRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .build();
        openAI.audios().transcribe(audioRequest);
        assertEquals(AudioRespFmt.JSON, audioRequest.getResponseFormat());
    }

    @Test
    void shouldKeepRespFmtWhenCallingTranscribeMethodOfAudioServiceWithJsonishRespFmt() {
        var audioRequest = AudioTranscribeRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .responseFormat(AudioRespFmt.VERBOSE_JSON)
                .build();
        openAI.audios().transcribe(audioRequest);
        assertEquals(AudioRespFmt.VERBOSE_JSON, audioRequest.getResponseFormat());
    }

    @Test
    void shouldThrowExceptionWhenCallingTranscribeMethodOfAudioServiceWithTextishRespFmt() {
        var audioRequest = AudioTranscribeRequest.builder()
                .file(Path.of("src/demo/resources/hello_audio.mp3"))
                .model("test_model")
                .responseFormat(AudioRespFmt.TEXT)
                .build();
        Exception exception = assertThrows(SimpleUncheckedException.class,
                () -> openAI.audios().transcribe(audioRequest));
        assertTrue(exception.getMessage().contains("Unexpected responseFormat for the method"));
    }
}
