package io.github.sashirestela.openai.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.metadata.Metadata;

public class URLBuilderTest {

  Metadata metadata = mock(Metadata.class);
  URLBuilder urlBuilder = new URLBuilder(metadata);

  @Test
  void shouldReturnUrlWithoutChangesWhenDoesNotContainPathOrQueryParams() {
    var methodName = "testMethod";
    var url = "/api/domain/entities";
    Map<String, List<Metadata.Parameter>> paramsMap = Map.of(
        "Path", new ArrayList<Metadata.Parameter>(),
        "Query", new ArrayList<Metadata.Parameter>());
    var methodMetadata = Metadata.Method.builder()
        .name(methodName)
        .url(url)
        .parametersByType(paramsMap)
        .build();
    var mapMethods = Map.of(methodName, methodMetadata);

    when(metadata.getMethods()).thenReturn(mapMethods);

    var actualUrl = urlBuilder.build(methodName, null);
    var expectedUrl = url;
    assertEquals(expectedUrl, actualUrl);
  }

  @Test
  void shouldReturnReplacedUrlWithPathParamsWhenUrlContainsPathParams() {
    var methodName = "testMethod";
    var url = "/api/domain/entities/{entityId}/details/{detailId}";
    var paramsList = List.of(
        Metadata.Parameter.builder()
            .index(1)
            .annotationValue("entityId")
            .build(),
        Metadata.Parameter.builder()
            .index(3)
            .annotationValue("detailId")
            .build());
    var paramsMap = Map.of(
        "Path", paramsList,
        "Query", new ArrayList<Metadata.Parameter>());
    var methodMetadata = Metadata.Method.builder()
        .name(methodName)
        .url(url)
        .parametersByType(paramsMap)
        .build();
    var mapMethods = Map.of(methodName, methodMetadata);

    when(metadata.getMethods()).thenReturn(mapMethods);

    var actualUrl = urlBuilder.build(methodName, new Object[] { null, 101, null, 201 });
    var expectedUrl = "/api/domain/entities/101/details/201";
    assertEquals(expectedUrl, actualUrl);
  }

  @Test
  void shouldReturnReplacedUrlWithQueryParamsWhenMethodContainsQueryParams() {
    var methodName = "testMethod";
    var url = "/api/domain/entities";
    var paramsList = List.of(
        Metadata.Parameter.builder()
            .index(1)
            .annotationValue("sortedBy")
            .build(),
        Metadata.Parameter.builder()
            .index(2)
            .annotationValue("filterBy")
            .build(),
        Metadata.Parameter.builder()
            .index(3)
            .annotationValue("rowsPerPage")
            .build());
    var paramsMap = Map.of(
        "Path", new ArrayList<Metadata.Parameter>(),
        "Query", paramsList);
    var methodMetadata = Metadata.Method.builder()
        .name(methodName)
        .url(url)
        .parametersByType(paramsMap)
        .build();
    var mapMethods = Map.of(methodName, methodMetadata);

    when(metadata.getMethods()).thenReturn(mapMethods);

    var actualUrl = urlBuilder.build(methodName, new Object[] { null, "name", null, 20 });
    var expectedUrl = "/api/domain/entities?sortedBy=name&rowsPerPage=20";
    assertEquals(expectedUrl, actualUrl);
  }
}