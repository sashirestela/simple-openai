package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.base.RealtimeConfig;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.openai.domain.realtime.ClientEvent;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession;
import io.github.sashirestela.openai.domain.realtime.ServerEvent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class RealtimeDemo {

    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) throws LineUnavailableException {
        var sound = new Sound();

        var openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .realtimeConfig(RealtimeConfig.of("gpt-4o-mini-realtime-preview"))
                .build();

        var session = RealtimeSession.builder()
                .modality(Modality.AUDIO)
                .instructions("Respond with short, direct sentences.")
                .voice(RealtimeSession.VoiceRealtime.ECHO)
                .outputAudioFormat(RealtimeSession.AudioFormatRealtime.PCM16)
                .inputAudioTranscription(null)
                .turnDetection(null)
                .temperature(0.9)
                .build();

        var realtime = openAI.realtime();

        realtime.onEvent(ServerEvent.ResponseAudioDelta.class, event -> {
            var dataBase64 = Base64.getDecoder().decode(event.getDelta());
            sound.speaker.write(dataBase64, 0, dataBase64.length);
        });

        realtime.onEvent(ServerEvent.ResponseAudioDone.class, event -> {
            delay(1000); // Some delay to receive trailing audio deltas
            sound.speaker.stop();
            sound.speaker.drain();
        });

        realtime.onEvent(ServerEvent.ResponseAudioTranscriptDone.class, event -> {
            System.out.println(event.getTranscript());
            askForSpeaking();
        });

        // Connect synchronously and wait for the connection to complete
        realtime.connect().thenRun(() -> {
            System.out.println("Connection established!");
            System.out.println("(Press any key and Return to terminate)");
            realtime.send(ClientEvent.SessionUpdate.of(session)).join();
        }).join();

        Scanner scanner = new Scanner(System.in);
        askForSpeaking();
        while (true) {
            sound.microphone.start();
            AtomicBoolean isRecording = new AtomicBoolean(true);
            CompletableFuture<Void> recordingFuture = CompletableFuture.runAsync(() -> {
                byte[] data = new byte[BUFFER_SIZE];
                try {
                    while (isRecording.get()) {
                        int bytesRead = sound.microphone.read(data, 0, data.length);
                        if (bytesRead > 0) {
                            var dataBase64 = Base64.getEncoder().encodeToString(data);
                            // Use runAsync to prevent blocking and add a small delay
                            CompletableFuture.runAsync(() -> {
                                delay(10); // Small delay to prevent rapid sending
                                realtime.send(ClientEvent.InputAudioBufferAppend.of(dataBase64)).join();
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            var keyPressed = scanner.nextLine();
            if (keyPressed.isEmpty()) {
                isRecording.set(false);
                sound.microphone.stop();
                sound.microphone.drain();

                // Wait for recording to finish
                recordingFuture.join();

                // Send ResponseCreate and wait for it to complete
                realtime.send(ClientEvent.ResponseCreate.of(null)).join();

                System.out.println("Waiting for AI response...\n");
                sound.speaker.start();
            } else {
                break;
            }
        }
        scanner.close();
        sound.cleanup();
        realtime.disconnect();
    }

    private static void askForSpeaking() {
        System.out.println("\nSpeak your question (press Return when done):");
    }

    private static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
