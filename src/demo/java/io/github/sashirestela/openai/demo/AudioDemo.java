package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.audio.AudioFormat;
import io.github.sashirestela.openai.common.audio.Voice;
import io.github.sashirestela.openai.domain.audio.AudioResponseFormat;
import io.github.sashirestela.openai.domain.audio.SpeechRequest;
import io.github.sashirestela.openai.domain.audio.TranscriptionRequest;
import io.github.sashirestela.openai.domain.audio.TranscriptionRequest.TimestampGranularity;
import io.github.sashirestela.openai.domain.audio.TranslationRequest;

import java.io.FileOutputStream;
import java.nio.file.Paths;

public class AudioDemo extends AbstractDemo {

    private static final String MODEL_TTS = "tts-1";
    private static final String MODEL = "whisper-1";

    private String speechFileName;
    private String fileName;

    public AudioDemo() {
        this.speechFileName = "src/demo/resources/response.mp3";
        this.fileName = "src/demo/resources/hello_audio.mp3";
    }

    public void demoCallAudioSpeech() {
        var speechRequest = SpeechRequest.builder()
                .model(MODEL_TTS)
                .input("Hello world, welcome to the AI universe!")
                .voice(Voice.ALLOY)
                .responseFormat(AudioFormat.MP3)
                .speed(1.0)
                .build();
        var futureSpeech = openAI.audios().speak(speechRequest);
        var speechResponse = futureSpeech.join();
        try (var audioFile = new FileOutputStream(speechFileName)) {
            audioFile.write(speechResponse.readAllBytes());
            System.out.println(audioFile.getChannel().size() + " bytes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demoCallAudioTranscription() {
        var audioRequest = TranscriptionRequest.builder()
                .file(Paths.get(fileName))
                .model(MODEL)
                .responseFormat(AudioResponseFormat.VERBOSE_JSON)
                .temperature(0.2)
                .timestampGranularity(TimestampGranularity.WORD)
                .timestampGranularity(TimestampGranularity.SEGMENT)
                .build();
        var futureAudio = openAI.audios().transcribe(audioRequest);
        var audioResponse = futureAudio.join();
        System.out.println(audioResponse);
    }

    public void demoCallAudioTranslation() {
        var audioRequest = TranslationRequest.builder()
                .file(Paths.get(fileName))
                .model(MODEL)
                .build();
        var futureAudio = openAI.audios().translate(audioRequest);
        var audioResponse = futureAudio.join();
        System.out.println(audioResponse);
    }

    public void demoCallAudioTranscriptionPlain() {
        var audioRequest = TranscriptionRequest.builder()
                .file(Paths.get(fileName))
                .model(MODEL)
                .build();
        var futureAudio = openAI.audios().transcribePlain(audioRequest);
        var audioResponse = futureAudio.join();
        System.out.println(audioResponse);
    }

    public void demoCallAudioTranslationPlain() {
        var audioRequest = TranslationRequest.builder()
                .file(Paths.get(fileName))
                .model(MODEL)
                .responseFormat(AudioResponseFormat.VTT)
                .build();
        var futureAudio = openAI.audios().translatePlain(audioRequest);
        var audioResponse = futureAudio.join();
        System.out.println(audioResponse);
    }

    public static void main(String[] args) {
        var demo = new AudioDemo();

        demo.addTitleAction("Call Audio Speech", demo::demoCallAudioSpeech);
        demo.addTitleAction("Call Audio Transcription", demo::demoCallAudioTranscription);
        demo.addTitleAction("Call Audio Translation", demo::demoCallAudioTranslation);
        demo.addTitleAction("Call Audio Transcription Plain", demo::demoCallAudioTranscriptionPlain);
        demo.addTitleAction("Call Audio Translation Plain", demo::demoCallAudioTranslationPlain);

        demo.run();
    }

}
