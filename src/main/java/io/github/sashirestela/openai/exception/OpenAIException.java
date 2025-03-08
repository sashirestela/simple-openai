package io.github.sashirestela.openai.exception;

public class OpenAIException extends RuntimeException {

    protected final transient OpenAIResponseInfo responseInfo;

    public OpenAIException(OpenAIResponseInfo responseInfo) {
        super(responseInfo.getErrorResponse().getError().getMessage());
        this.responseInfo = responseInfo;
    }

    public OpenAIResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public static class BadRequestException extends OpenAIException {

        public BadRequestException(OpenAIResponseInfo responseInfo) {
            super(responseInfo);
        }

    }

    public static class AuthenticationException extends OpenAIException {

        public AuthenticationException(OpenAIResponseInfo responseInfo) {
            super(responseInfo);
        }

    }

    public static class PermissionDeniedException extends OpenAIException {

        public PermissionDeniedException(OpenAIResponseInfo responseInfo) {
            super(responseInfo);
        }

    }

    public static class NotFoundException extends OpenAIException {

        public NotFoundException(OpenAIResponseInfo responseInfo) {
            super(responseInfo);
        }

    }

    public static class UnprocessableEntityException extends OpenAIException {

        public UnprocessableEntityException(OpenAIResponseInfo responseInfo) {
            super(responseInfo);
        }

    }

    public static class RateLimitException extends OpenAIException {

        public RateLimitException(OpenAIResponseInfo responseInfo) {
            super(responseInfo);
        }

    }

    public static class InternalServerException extends OpenAIException {

        public InternalServerException(OpenAIResponseInfo responseInfo) {
            super(responseInfo);
        }

    }

    public static class UnexpectedStatusCodeException extends OpenAIException {

        public UnexpectedStatusCodeException(OpenAIResponseInfo responseInfo) {
            super(responseInfo);
        }

    }

}
