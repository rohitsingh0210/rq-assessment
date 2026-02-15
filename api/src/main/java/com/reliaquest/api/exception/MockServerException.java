package com.reliaquest.api.exception;

public class MockServerException  extends RuntimeException {

    public MockServerException(String message) {
        super(message);
    }

    public MockServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
