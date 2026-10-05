package com.roadsafety.game.exception;

import org.springframework.http.HttpStatus;

/** Business-rule error carrying the HTTP status to return (404, 409, 400 ...). */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
