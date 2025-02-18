package io.github.sashirestela.openai.demo;

import com.google.auth.oauth2.GoogleCredentials;
import io.github.sashirestela.openai.support.GeminiAccessToken;
import java.io.FileInputStream;
import java.io.IOException;

public class ChatGeminiVertexDemo extends ChatDemo {
    static GeminiAccessToken geminiAccessToken;
    static {
        try {
            String credentialsPath = System.getenv("GEMINI_VERTEX_SA_CREDS_PATH");
            GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialsPath))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");

            geminiAccessToken = new GeminiAccessToken(credentials);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Google credentials", e);
        }
    }

    public ChatGeminiVertexDemo(String model) {
        super("gemini_vertex", model, null);
        this.chatProvider = this.openAIGeminiVertex;
    }

    static String getApiKey() {
        return geminiAccessToken.get();
    }

    public static void main(String[] args) {
        var demo = new ChatGeminiVertexDemo("google/gemini-1.5-flash");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImage);
        demo.addTitleAction("Call Chat with Structured Outputs", demo::demoCallChatWithStructuredOutputs);
        demo.addTitleAction("Call Chat with Structured Outputs 2", demo::demoCallChatWithStructuredOutputs2);

        demo.run();
    }

}
