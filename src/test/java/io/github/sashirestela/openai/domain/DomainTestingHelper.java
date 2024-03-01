package io.github.sashirestela.openai.domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DomainTestingHelper {

    private static DomainTestingHelper helper = null;

    private DomainTestingHelper() {
    }

    public static DomainTestingHelper get() {
        if (helper == null) {
            helper = new DomainTestingHelper();
        }
        return helper;
    }

    public void mockForStream(HttpClient httpClient, String responseFilePath) throws IOException {
        HttpResponse<Stream<String>> httpResponse = mock(HttpResponse.class);
        var listResponse = Files.readAllLines(Paths.get(responseFilePath));
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofLines().getClass())))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));
        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponse.body()).thenReturn(listResponse.stream());
    }

    public void mockForObject(HttpClient httpClient, String responseFilePath) throws IOException {
        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        var jsonResponse = Files.readAllLines(Paths.get(responseFilePath)).get(0);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));
        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponse.body()).thenReturn(jsonResponse);
    }

    public void mockForBinary(HttpClient httpClient, String responseFilePath) throws IOException {
        HttpResponse<InputStream> httpResponse = mock(HttpResponse.class);
        InputStream binaryResponse = new FileInputStream(responseFilePath);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofInputStream().getClass())))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));
        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponse.body()).thenReturn(binaryResponse);
    }

}
