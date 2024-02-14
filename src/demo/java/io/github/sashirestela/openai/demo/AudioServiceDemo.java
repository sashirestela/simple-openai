package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.audio.AudioRespFmt;
import io.github.sashirestela.openai.domain.audio.AudioSpeechRequest;
import io.github.sashirestela.openai.domain.audio.AudioTranscribeRequest;
import io.github.sashirestela.openai.domain.audio.AudioTranslateRequest;
import io.github.sashirestela.openai.domain.audio.SpeechRespFmt;
import io.github.sashirestela.openai.domain.audio.Voice;
import java.io.FileOutputStream;
import java.nio.file.Paths;

public class AudioServiceDemo extends AbstractDemo {
    private static final String MODEL_TTS = "tts-1";
    private static final String MODEL = "whisper-1";

    private String speechFileName;
    private String fileName;

    public AudioServiceDemo() {
        this.speechFileName = "src/demo/resources/response.mp3";
        this.fileName = "src/demo/resources/hello_audio.mp3";
    }

    public void demoCallAudioSpeech() {
        var speechRequest = AudioSpeechRequest.builder()
                .model(MODEL_TTS)
                .input("Hello world, welcome to the AI universe!")
                .voice(Voice.ALLOY)
                .responseFormat(SpeechRespFmt.MP3)
                .speed(1.0)
                .build();
        var futureSpeech = openAI.audios().speak(speechRequest);
        var speechResponse = futureSpeech.join();
        try {
            var audioFile = new FileOutputStream(speechFileName);
            audioFile.write(speechResponse.readAllBytes());
            System.out.println(audioFile.getChannel().size() + " bytes");
            audioFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demoCallAudioTranscription() {
        var audioRequest = AudioTranscribeRequest.builder()
                .file(Paths.get(fileName))
                .model(MODEL)
                .responseFormat(AudioRespFmt.VERBOSE_JSON)
                .build();
        var futureAudio = openAI.audios().transcribe(audioRequest);
        var audioResponse = futureAudio.join();
        System.out.println(audioResponse);
    }

    public void demoCallAudioTranslation() {
        var audioRequest = AudioTranslateRequest.builder()
                .file(Paths.get(fileName))
                .model(MODEL)
                .build();
        var futureAudio = openAI.audios().translate(audioRequest);
        var audioResponse = futureAudio.join();
        System.out.println(audioResponse);
    }

    public void demoCallAudioTranscriptionPlain() {
        var audioRequest = AudioTranscribeRequest.builder()
                .file(Paths.get(fileName))
                .model(MODEL)
                .build();
        var futureAudio = openAI.audios().transcribePlain(audioRequest);
        var audioResponse = futureAudio.join();
        System.out.println(audioResponse);
    }

    public void demoCallAudioTranslationPlain() {
        var audioRequest = AudioTranslateRequest.builder()
                .file(Paths.get(fileName))
                .model(MODEL)
                .responseFormat(AudioRespFmt.VTT)
                .build();
        var futureAudio = openAI.audios().translatePlain(audioRequest);
        var audioResponse = futureAudio.join();
        System.out.println(audioResponse);
    }

    public static void main(String[] args) {
        var demo = new AudioServiceDemo();

        demo.addTitleAction("Call Audio Speech", demo::demoCallAudioSpeech);
        demo.addTitleAction("Call Audio Transcription", demo::demoCallAudioTranscription);
        demo.addTitleAction("Call Audio Translation", demo::demoCallAudioTranslation);
        demo.addTitleAction("Call Audio Transcription Plain", demo::demoCallAudioTranscriptionPlain);
        demo.addTitleAction("Call Audio Translation Plain", demo::demoCallAudioTranslationPlain);

        demo.run();
    }
}