package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.support.GeminiAccessToken;

public class ChatGeminiVertexDemo extends ChatDemo {

    static String MODEL = "google/gemini-1.5-flash";

    // GEMINI_SA_CREDS_PATH is the path to the Google service account credentials file in JSON format. See
    static GeminiAccessToken geminiAccessToken = new GeminiAccessToken(System.getenv("GEMINI_VERTEX_SA_CREDS_PATH"));

    public ChatGeminiVertexDemo(String model) {
        super("gemini_vertex", model, null);
        this.chatProvider = this.openAIGeminiVertex;
    }

    static String getApiKey() {
        return geminiAccessToken.get();
    }

    public static void main(String[] args) {
        var demo = new ChatGeminiVertexDemo(MODEL);

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        /* requires GCS file path. Didn't test */
        //demo.addTitleAction("Call Chat with Vision (External image)", demo::demoCallChatWithVisionExternalImage);
        demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImage);
        demo.addTitleAction("Call Chat with Structured Outputs", demo::demoCallChatWithStructuredOutputs);
        demo.addTitleAction("Call Chat with Structured Outputs 2", demo::demoCallChatWithStructuredOutputs2);

        demo.run();
    }

}
