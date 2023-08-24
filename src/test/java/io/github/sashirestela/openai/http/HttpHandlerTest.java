package io.github.sashirestela.openai.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.DELETE;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.POST;
import io.github.sashirestela.openai.http.annotation.PUT;
import io.github.sashirestela.openai.http.annotation.Path;
import io.github.sashirestela.openai.support.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ExtendWith(MockitoExtension.class)
public class HttpHandlerTest {

  @Mock
  HttpClient httpClient;
  @Mock
  HttpResponse<String> httpResponse;
  @Mock
  HttpResponse<Stream<String>> httpResponseStream;

  InvocationHandler handler;
  TestService service;

  @BeforeEach
  void setup() {
    handler = new HttpHandler(
        HttpConfig.builder()
            .apiKey("apiKey")
            .urlBase("https://api")
            .httpClient(httpClient)
            .build(),
        null);
    service = ReflectUtil.get().createProxy(TestService.class, handler);
  }

  @Test
  void shouldThrowExceptionWhenMethodIsAnnotatedWithNoHttpAnnotation() {
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> service.getTestDataNotAnnotated());

    String actualMessage = exception.getCause().getMessage();
    String expectedMessage = "Missing HTTP anotation for the method getTestDataNotAnnotated.";

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void shouldThrownAnExceptionWhenThereIsPathParamInUrlAndThereAreNoArgumentsInMethod() {
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> service.deleteDemo());

    String actualMessage = exception.getCause().getMessage();
    String expectedMessage = "Path param in the url requires at least an argument in the method deleteDemo.";

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void shouldThrownAnExceptionWhenThereIsPathParamInUrlAndThereAreNoAnnotatedArgumentsInMethod() {
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> service.updateDemo(new RequestDemo("prefix", 10, true)));

    String actualMessage = exception.getCause().getMessage();
    String expectedMessage = "Path param in the url requires at least an annotated argument in the method updateDemo.";

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void shouldThrownAnExceptionWhenThereIsPathParamInUrlAndDoesNotMatchAnnotatedArgumentInMethod() {
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> service.createDemo(10, new RequestDemo("prefix", 10, true)));

    String actualMessage = exception.getCause().getMessage();
    String expectedMessage = "Path param demoId in the url cannot find an annotated argument in the method createDemo.";

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldReturnAnObjectWhenEverythingIsFine() throws IOException, InterruptedException {
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn("{\"id\":100,\"description\":\"Description\",\"active\":true}");

    Demo actualDemo = service.getDemo(100).join();
    Demo expectedDemo = new Demo(100, "Description", true);

    assertEquals(expectedDemo.getId(), actualDemo.getId());
    assertEquals(expectedDemo.getDescription(), actualDemo.getDescription());
    assertEquals(expectedDemo.isActive(), actualDemo.isActive());
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldReturnAListWhenEverythingIsFine() throws IOException, InterruptedException {
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn("{\"object\":\"list\",\"data\":[{\"id\":100,\"description\":\"Description\",\"active\":true}]}");

    List<Demo> actualListDemo = service.getDemos().join();
    Demo actualDemo = actualListDemo.get(0);
    Demo expectedDemo = new Demo(100, "Description", true);

    assertEquals(expectedDemo.getId(), actualDemo.getId());
    assertEquals(expectedDemo.getDescription(), actualDemo.getDescription());
    assertEquals(expectedDemo.isActive(), actualDemo.isActive());
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldReturnAStreamWhenEverythingIsFine() throws IOException, InterruptedException {
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofLines().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponseStream));
    when(httpResponseStream.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponseStream.body()).thenReturn(Stream.of("data: {\"id\":100,\"content\":\"Description\",\"active\":true}"));

    Stream<Demo> actualStreamDemo = service.getDemoStream(new RequestDemo("Descr", 10, true)).join();
    Demo actualDemo = actualStreamDemo.findFirst().get();
    Demo expectedDemo = new Demo(100, "Description", true);

    assertEquals(expectedDemo.getId(), actualDemo.getId());
    assertEquals(expectedDemo.getDescription(), actualDemo.getDescription());
    assertEquals(expectedDemo.isActive(), actualDemo.isActive());
  }

  @Test
  void shouldThrownAnExceptionWhenMethodReturnTypeIsNotExpected() {
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> service.getSetDemos());

    String actualMessage = exception.getCause().getMessage();
    String expectedMessage = "Unsupported return type for method getSetDemos of the class TestService.";

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldThrowExceptionWhenServerAnswerWithError() throws IOException, InterruptedException {
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
    when(httpResponse.body()).thenReturn("{ \"error\": { \"message\": \"The demo 531 does not exist\", \"type\": \"invalid_request_error\", \"param\": \"demo\", \"code\": \"demo_not_found\" } }");

    CompletionException exception = assertThrows(CompletionException.class,
        () -> service.getDemo(531).join());

    assertTrue(exception.getCause().getMessage().contains("The demo 531 does not exist"));
  }

  static interface TestService {

    String getTestDataNotAnnotated();

    @DELETE("/demos/{demoId}")
    CompletableFuture<Set<Demo>> deleteDemo();

    @PUT("/demos/{demoId}")
    CompletableFuture<Demo> updateDemo(@Body RequestDemo request);

    @PUT("/demos/{demoId}")
    CompletableFuture<Demo> createDemo(@Path("demo_id") int demoId, @Body RequestDemo request);

    @GET("/demos/{demoId}")
    CompletableFuture<Demo> getDemo(@Path("demoId") int demoId);

    @GET("/demos")
    CompletableFuture<List<Demo>> getDemos();

    @POST("/demos")
    CompletableFuture<Stream<Demo>> getDemoStream(@Body RequestDemo request);

    @GET("/demo")
    CompletableFuture<Set<Demo>> getSetDemos();

  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  static class Demo {

    private int id;

    @JsonAlias({ "content" })
    private String description;

    private boolean active;

  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public static class RequestDemo {

    private String prefix;

    private int total;

    private boolean stream;

    public void setStream(boolean stream) {
      this.stream = stream;
    }

  }

}