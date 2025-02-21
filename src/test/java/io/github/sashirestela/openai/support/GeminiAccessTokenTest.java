package io.github.sashirestela.openai.support;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiAccessTokenTest {

    @Mock
    private GoogleCredentials mockGoogleCredentials;

    @InjectMocks
    private GeminiAccessToken geminiAccessToken;

    @BeforeEach
    void setUp() {
        geminiAccessToken = new GeminiAccessToken(mockGoogleCredentials);
    }

    @Test
    void testGetAccessToken_Success() throws IOException {
        AccessToken mockAccessToken = new AccessToken("mock-token", new Date());
        when(mockGoogleCredentials.getAccessToken()).thenReturn(mockAccessToken);

        String token = geminiAccessToken.get();

        assertEquals("mock-token", token);
        verify(mockGoogleCredentials, times(1)).refresh();
    }

    @Test
    void testGetAccessToken_WhenCredentialsAreInvalid() {
        var invalidToken = new GeminiAccessToken("no-such-file");
        assertEquals("", invalidToken.get());
    }

    @Test
    void testGetAccessToken_WhenCredentialsAreNull() {
        var invalidToken = new GeminiAccessToken((GoogleCredentials) null);
        assertEquals("", invalidToken.get());
    }

    @Test
    void testGetAccessToken_WhenRefreshFails() throws IOException {
        doThrow(new IOException("Refresh failed")).when(mockGoogleCredentials).refresh();

        assertEquals("", geminiAccessToken.get());
    }

    @Test
    void testGetAccessToken_WhenAccessTokenIsNull() {
        when(mockGoogleCredentials.getAccessToken()).thenReturn(null);

        assertEquals("", geminiAccessToken.get());
    }

    @Test
    void testConstructorWithValidCredentialsFile() throws IOException {
        var mockCreds = "{"
                + "\"type\": \"service_account\","
                + "\"project_id\": \"test-project\","
                + "\"private_key_id\": \"1234567890abcdef\","
                + "\"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBA...\\n-----END PRIVATE KEY-----\\n\","
                + "\"client_email\": \"test-account@test-project.iam.gserviceaccount.com\","
                + "\"client_id\": \"123456789012345678901\","
                + "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\","
                + "\"token_uri\": \"https://oauth2.googleapis.com/token\","
                + "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\","
                + "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/test-account@test-project.iam.gserviceaccount.com\""
                + "}";

        var mockInputStream = new ByteArrayInputStream(mockCreds.getBytes());

        var mockCredentials = mock(ServiceAccountCredentials.class);
        when(mockCredentials.createScoped(
                Collections.singletonList("https://www.googleapis.com/auth/cloud-platform")))
                .thenReturn(mockCredentials);

        try (
                var mockedStatic = mockStatic(ServiceAccountCredentials.class);
                var mockFileInputStream = mockConstruction(FileInputStream.class,
                        (mock, context) -> when(mock.read(any(byte[].class))).thenAnswer(
                                invocation -> mockInputStream.read(invocation.getArgument(0))))) {
            mockedStatic.when(
                    () -> ServiceAccountCredentials.fromStream(any(FileInputStream.class)))
                    .thenReturn(mockCredentials);

            var token = new GeminiAccessToken("mock-file-path.json");

            assertNotNull(token.getCredentials(),
                    "Expected credentials to be initialized, but got null");

            verify(mockCredentials, times(1))
                    .createScoped(
                            Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
        }
    }

    @Test
    void testConstructorWithEmptyFilePath() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> new GeminiAccessToken(""));

        assertEquals("Credentials file path is empty", exception.getMessage());
    }

    @Test
    void testConstructorWithFileNotFound() {
        var token = new GeminiAccessToken("no-such-file.json");
        assertNull(token.getCredentials(),
                "Expected credentials to be null when file is not found");
    }

}
