package io.github.sashirestela.openai.domain.audio;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class AudioDomainTest {

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
    void testAudiosSpeech() throws IOException {
        DomainTestingHelper.get().mockForBinary(httpClient, "src/test/resources/audios_speak.mp3");
        var speechRequest = AudioSpeechRequest.builder()
                .model("tts-1")
                .input("Hello world, welcome to the AI universe!")
                .voice(Voice.ALLOY)
                .responseFormat(SpeechRespFmt.MP3)
                .speed(1.0)
                .build();
        var speechResponse = openAI.audios().speak(speechRequest).join();
        System.out.println(speechResponse.readAllBytes().length);
        assertNotNull(speechResponse);
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
                .timestampGranularity(TimestampGranularity.WORD)
                .timestampGranularity(TimestampGranularity.SEGMENT)
                .language("en")
                .build();
        var audioResponse = openAI.audios().transcribe(audioRequest).join();
        System.out.println(audioResponse);
        assertNotNull(audioResponse);
    }

    @Test
    void testAudiosTranslate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/audios_translate.json");
        var audioRequest = AudioTranslateRequest.builder()
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
        var audioRequest = AudioTranslateRequest.builder()
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
