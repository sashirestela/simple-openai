package io.github.sashirestela.openai.exception;

import io.github.sashirestela.cleverclient.ExceptionConverter;
import io.github.sashirestela.cleverclient.ResponseInfo;
import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.openai.exception.OpenAIException.AuthenticationException;
import io.github.sashirestela.openai.exception.OpenAIException.BadRequestException;
import io.github.sashirestela.openai.exception.OpenAIException.InternalServerException;
import io.github.sashirestela.openai.exception.OpenAIException.NotFoundException;
import io.github.sashirestela.openai.exception.OpenAIException.PermissionDeniedException;
import io.github.sashirestela.openai.exception.OpenAIException.RateLimitException;
import io.github.sashirestela.openai.exception.OpenAIException.UnexpectedStatusCodeException;
import io.github.sashirestela.openai.exception.OpenAIException.UnprocessableEntityException;
import io.github.sashirestela.openai.exception.OpenAIResponseInfo.OpenAIErrorResponse;

public class OpenAIExceptionConverter extends ExceptionConverter {

    public static void rethrow(Throwable e) {
        throw new OpenAIExceptionConverter().convert(e);
    }

    @Override
    public RuntimeException convertHttpException(ResponseInfo responseInfo) {
        var openAIResponseInfo = mapResponseInfo(responseInfo);
        var status = openAIResponseInfo.getStatus();
        switch (status) {
            case 400:
                return new BadRequestException(openAIResponseInfo);
            case 401:
                return new AuthenticationException(openAIResponseInfo);
            case 403:
                return new PermissionDeniedException(openAIResponseInfo);
            case 404:
                return new NotFoundException(openAIResponseInfo);
            case 422:
                return new UnprocessableEntityException(openAIResponseInfo);
            case 429:
                return new RateLimitException(openAIResponseInfo);
            default:
                if (status >= 500) {
                    return new InternalServerException(openAIResponseInfo);
                } else {
                    return new UnexpectedStatusCodeException(openAIResponseInfo);
                }
        }
    }

    private OpenAIResponseInfo mapResponseInfo(ResponseInfo responseInfo) {
        var requestInfo = responseInfo.getRequest();
        return OpenAIResponseInfo.builder()
                .status(responseInfo.getStatusCode())
                .errorResponse(JsonUtil.jsonToObject(responseInfo.getData(), OpenAIErrorResponse.class))
                .httpMethod(requestInfo.getHttpMethod())
                .url(requestInfo.getUrl())
                .responseHeaders(responseInfo.getHeaders())
                .requestHeaders(requestInfo.getHeaders())
                .build();
    }

}
