package com.blogging_platform.util;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public class PostException {
    private String message;
    private Instant timestamp;
    private HttpStatus status;

    public PostException(String message, Instant timestamp, HttpStatus status) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
