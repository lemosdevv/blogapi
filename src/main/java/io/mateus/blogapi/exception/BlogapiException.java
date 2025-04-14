package io.mateus.blogapi.exception;

import org.springframework.http.HttpStatus;

public class BlogapiException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private final HttpStatus status;
    private final String message;

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public BlogapiException(HttpStatus httpStatus, String message) {
        super();
        this.status = httpStatus;
        this.message = message;
    }
}
