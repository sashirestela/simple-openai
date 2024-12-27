package io.github.sashirestela.openai.support;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

import static io.github.sashirestela.cleverclient.util.CommonUtil.isNullOrEmpty;

public class GeminiAccessToken {

    private GoogleCredentials credentials;

    public GeminiAccessToken(String credentialsFilePath) {
        if (isNullOrEmpty(credentialsFilePath)) {
            throw new IllegalArgumentException("Credentials file path is empty");
        }
        try {
            credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialsFilePath))
                    .createScoped(
                            Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            credentials = null;
        }
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

        var token = credentials.getAccessToken();
        return token.getTokenValue();
    }

}
