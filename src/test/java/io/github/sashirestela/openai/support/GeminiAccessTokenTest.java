package io.github.sashirestela.openai.support;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiAccessTokenTest {

    @Mock
    private GoogleCredentials mockCredentials;

    @InjectMocks
    private GeminiAccessToken geminiAccessToken;

    @BeforeEach
    void setUp() {
        geminiAccessToken = new GeminiAccessToken(mockCredentials);
    }

    @Test
    void testGetAccessToken_Success() throws IOException {
        AccessToken mockAccessToken = new AccessToken("mock-token", new Date());
        when(mockCredentials.getAccessToken()).thenReturn(mockAccessToken);

        String token = geminiAccessToken.get();

        assertEquals("mock-token", token);
        verify(mockCredentials, times(1)).refresh();
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
        doThrow(new IOException("Refresh failed")).when(mockCredentials).refresh();

        assertEquals("", geminiAccessToken.get());
    }

    @Test
    void testGetAccessToken_WhenAccessTokenIsNull() {
        when(mockCredentials.getAccessToken()).thenReturn(null);

        assertEquals("", geminiAccessToken.get());
    }

}
