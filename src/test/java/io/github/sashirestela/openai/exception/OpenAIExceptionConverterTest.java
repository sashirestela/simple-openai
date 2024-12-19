package io.github.sashirestela.openai.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.ResponseInfo;
import io.github.sashirestela.cleverclient.ResponseInfo.RequestInfo;
import io.github.sashirestela.cleverclient.support.CleverClientException;
import io.github.sashirestela.cleverclient.support.Configurator;
import io.github.sashirestela.openai.exception.OpenAIResponseInfo.OpenAIErrorResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenAIExceptionConverterTest {

    private OpenAIExceptionConverter converter;
    private ResponseInfo mockResponseInfo;
    private RequestInfo mockRequestInfo;
    private Map<String, List<String>> headers;

    @BeforeAll
    static void setup() {
        Configurator.builder().objectMapper(new ObjectMapper()).build();
    }

    @BeforeEach
    void setUp() {
        converter = new OpenAIExceptionConverter();
        mockResponseInfo = mock(ResponseInfo.class);
        mockRequestInfo = mock(RequestInfo.class);
        headers = new HashMap<>();
        headers.put("Content-Type", List.of("application/json"));

        // Setup common mock behaviors
        when(mockResponseInfo.getRequest()).thenReturn(mockRequestInfo);
        when(mockRequestInfo.getHttpMethod()).thenReturn("POST");
        when(mockRequestInfo.getUrl()).thenReturn("https://api.openai.com/v1/chat/completions");
        when(mockRequestInfo.getHeaders()).thenReturn(headers);
        when(mockResponseInfo.getHeaders()).thenReturn(headers);
    }

    @ParameterizedTest
    @CsvSource({
            "400, BadRequestException",
            "401, AuthenticationException",
            "403, PermissionDeniedException",
            "404, NotFoundException",
            "422, UnprocessableEntityException",
            "429, RateLimitException",
            "500, InternalServerException",
            "503, InternalServerException",
            "418, UnexpectedStatusCodeException"
    })
    void testConvertHttpException(int statusCode, String expectedExceptionClass) {
        // Arrange
        String errorJson = "{" +
                "    \"error\": {" +
                "        \"message\": \"Test error message\"," +
                "        \"type\": \"invalid_request_error\"," +
                "        \"param\": \"model\"," +
                "        \"code\": \"model_not_found\"" +
                "    }" +
                "}";

        when(mockResponseInfo.getStatusCode()).thenReturn(statusCode);
        when(mockResponseInfo.getData()).thenReturn(errorJson);

        // Act
        RuntimeException exception = converter.convertHttpException(mockResponseInfo);

        // Assert
        String expectedClassName = "io.github.sashirestela.openai.exception.OpenAIException$" + expectedExceptionClass;
        assertEquals(expectedClassName, exception.getClass().getName());

        OpenAIException openAIException = (OpenAIException) exception;
        OpenAIResponseInfo responseInfo = openAIException.getResponseInfo();

        assertEquals(statusCode, responseInfo.getStatus());
        assertEquals("POST", responseInfo.getHttpMethod());
        assertEquals("https://api.openai.com/v1/chat/completions", responseInfo.getUrl());
        assertEquals(headers, responseInfo.getRequestHeaders());
        assertEquals(headers, responseInfo.getResponseHeaders());

        OpenAIErrorResponse errorResponse = responseInfo.getErrorResponse();
        assertNotNull(errorResponse);
        assertEquals("Test error message", errorResponse.getError().getMessage());
        assertEquals("invalid_request_error", errorResponse.getError().getType());
        assertEquals("model", errorResponse.getError().getParam());
        assertEquals("model_not_found", errorResponse.getError().getCode());
    }

    @Test
    void testRethrow() {
        // Arrange
        String errorJson = "{" +
                "    \"error\": {" +
                "        \"message\": \"Rate limit exceeded\"," +
                "        \"type\": \"rate_limit_error\"," +
                "        \"param\": \"some_param\"," +
                "        \"code\": \"rate_limit_exceeded\"" +
                "    }" +
                "}";

        when(mockResponseInfo.getStatusCode()).thenReturn(429);
        when(mockResponseInfo.getData()).thenReturn(errorJson);

        // Act & Assert
        assertThrows(OpenAIException.RateLimitException.class, () -> {
            OpenAIExceptionConverter.rethrow(new CleverClientException(mockResponseInfo));
        });
    }

    @Test
    void testOpenAIResponseInfoBuilder() {
        // Arrange
        Map<String, List<String>> testHeaders = new HashMap<>();
        testHeaders.put("Authorization", List.of("Bearer test-token"));

        OpenAIErrorResponse errorResponse = OpenAIErrorResponse.builder()
                .error(OpenAIErrorResponse.OpenAIError.builder()
                        .message("Test message")
                        .type("test_type")
                        .param("test_param")
                        .code("test_code")
                        .build())
                .build();

        // Act
        OpenAIResponseInfo responseInfo = OpenAIResponseInfo.builder()
                .status(400)
                .errorResponse(errorResponse)
                .httpMethod("GET")
                .url("https://api.openai.com/v1/test")
                .responseHeaders(testHeaders)
                .requestHeaders(testHeaders)
                .build();

        // Assert
        assertEquals(400, responseInfo.getStatus());
        assertEquals(errorResponse, responseInfo.getErrorResponse());
        assertEquals("GET", responseInfo.getHttpMethod());
        assertEquals("https://api.openai.com/v1/test", responseInfo.getUrl());
        assertEquals(testHeaders, responseInfo.getResponseHeaders());
        assertEquals(testHeaders, responseInfo.getRequestHeaders());
        assertEquals("Test message", responseInfo.getErrorResponse().getError().getMessage());
        assertEquals("test_type", responseInfo.getErrorResponse().getError().getType());
        assertEquals("test_param", responseInfo.getErrorResponse().getError().getParam());
        assertEquals("test_code", responseInfo.getErrorResponse().getError().getCode());
    }

}
