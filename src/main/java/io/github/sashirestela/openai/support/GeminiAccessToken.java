package io.github.sashirestela.openai.support;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;

public class GeminiAccessToken {
    private static final String GOOGLE_SERVICE_ACCOUNT_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private final GoogleCredentials credentials;

    // Constructor takes credentials instead of reading from a file
    public GeminiAccessToken(GoogleCredentials credentials) {
        this.credentials = credentials;
    }

    public String get() {
        if (credentials == null) {
            return "";
        }
        try {
            credentials.refresh();
        } catch (IOException e) {
            return "";
        }

        if (credentials.getAccessToken() == null) {
            return "";
        }
        return credentials.getAccessToken().getTokenValue();
    }
}