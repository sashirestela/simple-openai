package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.SimpleOpenAI.RealtimeConfig;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.openai.domain.realtime.ClientEvent;
import io.github.sashirestela.openai.domain.realtime.Configuration;
import io.github.sashirestela.openai.domain.realtime.Configuration.AudioFormatRealtime;
import io.github.sashirestela.openai.domain.realtime.Configuration.VoiceRealtime;
import io.github.sashirestela.openai.domain.realtime.ServerEvent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import java.util.Base64;
import java.util.Scanner;

public class RealtimeDemo {

    private static final int BUFFER_SIZE = 512;

    public static void main(String[] args) throws LineUnavailableException {
        var sound = new Sound();

        var openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .realtimeConfig(RealtimeConfig.of("gpt-4o-realtime-preview-2024-10-01"))
                .build();
        var realtime = openAI.realtime();

        realtime.onEvent(ServerEvent.SessionCreated.class, event -> {
            realtime.send(ClientEvent.SessionUpdate.of(Configuration.builder()
                    .modality(Modality.TEXT)
                    .modality(Modality.AUDIO)
                    .instructions("Respond in a short and concise way.")
                    .voice(VoiceRealtime.ASH)
                    .inputAudioFormat(AudioFormatRealtime.PCM16)
                    .outputAudioFormat(AudioFormatRealtime.PCM16)
                    .inputAudioTranscription(null)
                    .turnDetection(null)
                    .temperature(0.9)
                    .build()))
                    .join();
        });

        realtime.onEvent(ServerEvent.ResponseAudioDelta.class, event -> {
            var dataBase64 = Base64.getDecoder().decode(event.getDelta());
            sound.speaker.write(dataBase64, 0, dataBase64.length);
        });

        realtime.onEvent(ServerEvent.ResponseAudioDone.class, event -> {
            sound.speaker.stop();
            sound.speaker.drain();
        });

        realtime.onEvent(ServerEvent.ResponseAudioTranscriptDone.class, event -> {
            System.out.println(event.getTranscript());
        });

        realtime.connect().join();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press any key but Return to terminate.");
        while (true) {
            System.out.println("Speak your question (press Return when done):");
            sound.microphone.start();
            Thread recordingThread = new Thread(() -> {
                byte[] data = new byte[BUFFER_SIZE];
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        int bytesRead = sound.microphone.read(data, 0, data.length);
                        if (bytesRead > 0) {
                            var dataBase64 = Base64.getEncoder().encodeToString(data);
                            realtime.send(ClientEvent.InputAudioBufferAppend.of(dataBase64)).join();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            recordingThread.start();
            var keyPressed = scanner.nextLine();
            if (keyPressed.isEmpty()) {
                recordingThread.interrupt();
                sound.microphone.stop();
                sound.microphone.drain();
                realtime.send(ClientEvent.ResponseCreate.of(null)).join();
                System.out.println("Waiting for AI response...");
                sound.speaker.start();
            } else {
                break;
            }
        }
        scanner.close();
        sound.cleanup();
    }

    public static class Sound {

        private static final float SAMPLE_RATE = 24000f;
        private static final int SAMPLE_SIZE_BITS = 16;
        private static final int CHANNELS = 1;
        private static final boolean SIGNED = true;
        private static final boolean BIG_ENDIAN = false;

        private TargetDataLine microphone;
        private SourceDataLine speaker;

        public Sound() throws LineUnavailableException {
            AudioFormat format = new AudioFormat(
                    SAMPLE_RATE,
                    SAMPLE_SIZE_BITS,
                    CHANNELS,
                    SIGNED,
                    BIG_ENDIAN);

            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(micInfo)) {
                throw new LineUnavailableException("Microphone not supported");
            }
            microphone = (TargetDataLine) AudioSystem.getLine(micInfo);
            microphone.open(format);

            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(speakerInfo)) {
                throw new LineUnavailableException("Speakers not supported");
            }
            speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            speaker.open(format);
        }

        public void cleanup() {
            microphone.stop();
            microphone.drain();
            microphone.close();

            speaker.stop();
            speaker.drain();
            speaker.close();
        }

    }

}
