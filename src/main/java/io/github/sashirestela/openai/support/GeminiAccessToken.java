package io.github.sashirestela.openai.support;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

import static io.github.sashirestela.cleverclient.util.CommonUtil.isNullOrEmpty;

public class GeminiAccessToken {

    private static final String GOOGLE_SERVICE_ACCOUNT_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    @Getter
    private final GoogleCredentials credentials;

    public GeminiAccessToken(String credentialsFilePath) {
        GoogleCredentials creds;
        if (isNullOrEmpty(credentialsFilePath)) {
            throw new IllegalArgumentException("Credentials file path is empty");
        }
        try {
            creds = ServiceAccountCredentials.fromStream(
                    new FileInputStream(credentialsFilePath))
                    .createScoped(
                            Collections.singletonList(GOOGLE_SERVICE_ACCOUNT_SCOPE));
        } catch (IOException e) {
            creds = null;
        }
        credentials = creds;
    }

    // Constructor takes credentials instead of reading from a file
    GeminiAccessToken(GoogleCredentials credentials) {
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
