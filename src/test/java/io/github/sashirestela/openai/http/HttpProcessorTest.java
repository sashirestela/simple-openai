package io.github.sashirestela.openai.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleUncheckedException;

@SuppressWarnings("unchecked")
public class HttpProcessorTest {

  HttpProcessor httpProcessor;
  HttpClient httpClient = mock(HttpClient.class);
  HttpResponse<String> httpResponse = mock(HttpResponse.class);

  @BeforeEach
  void init() {
    httpProcessor = HttpProcessor.builder()
        .urlBase("https://api.demmo")
        .headers(List.of("Authorization", "Bearer qwerty"))
        .httpClient(httpClient)
        .build();
  }

  @Test
  void shouldThownExceptionWhenCallingCreateMethodForNoAnnotedMethod() {
    Exception exception = assertThrows(SimpleUncheckedException.class,
        () -> httpProcessor.create(ITest.NotAnnotatedService.class, null));
    assertTrue(exception.getMessage().contains("Missing HTTP anotation for the method"));
  }

  @Test
  void shouldThownExceptionWhenCallingCreateMethodForBadPathParamMethod() {
    Exception exception = assertThrows(SimpleUncheckedException.class,
        () -> httpProcessor.create(ITest.BadPathParamService.class, null));
    assertTrue(exception.getMessage().contains("Path param demoId in the url cannot find"));
  }

  @Test
  void shouldSetInternalStateWhenCallingCreateMethodForWellFormedService() {
    httpProcessor.create(ITest.GoodService.class, null);
    assertNotNull(httpProcessor.getMetadata());
    assertNotNull(httpProcessor.getUrlBuilder());
    assertNotNull(httpProcessor.getHttpClient());
    assertNotNull(httpProcessor.getUrlBase());
  }

  @Test
  void shouldReturnAnObjectWhenMethodReturnTypeIsAnObject() {
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn("{\"id\":100,\"description\":\"Description\",\"active\":true}");
    
    var service = httpProcessor.create(ITest.GoodService.class, null);
    var actualDemo = service.getDemo(100).join();
    var expectedDemo = new ITest.Demo(100, "Description", true);
    
    assertEquals(expectedDemo, actualDemo);
  }

  @Test
  void shouldReturnAListWhenMethodReturnTypeIsAList() {
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn("{\"object\":\"list\",\"data\":[{\"id\":100,\"description\":\"Description\",\"active\":true}]}");

    var service = httpProcessor.create(ITest.GoodService.class, null);
    var actualListDemo = service.getDemos().join();
    var actualDemo = actualListDemo.get(0);
    var expectedDemo = new ITest.Demo(100, "Description", true);
    
    assertEquals(expectedDemo, actualDemo);
  }
}