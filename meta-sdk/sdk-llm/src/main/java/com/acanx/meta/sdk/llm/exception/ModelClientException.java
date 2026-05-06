package com.acanx.meta.sdk.llm.exception;

public class ModelClientException extends RuntimeException {

    private final int statusCode;
    private final String responseBody;

    public ModelClientException(String message) {
        this(message, -1, null, null);
    }

    public ModelClientException(String message, Throwable cause) {
        this(message, -1, null, cause);
    }

    public ModelClientException(String message, int statusCode, String responseBody) {
        this(message, statusCode, responseBody, null);
    }

    public ModelClientException(String message, int statusCode, String responseBody, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
