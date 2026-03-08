package com.blogging_platform.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ApiHandlerExceptions {

    @ExceptionHandler(value = {PostNotFoundException.class})
    public ResponseEntity<PostException> handlePostNotFoundException(PostNotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        PostException postException = new PostException(
                e.getMessage(),
                Instant.now(),
                status
        );

        return new ResponseEntity<>(postException, status);
    }

    @ExceptionHandler(value = {PostNotCreatedException.class})
    public ResponseEntity<PostException> handlePostNotCreatedException(PostNotCreatedException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        PostException postException = new PostException(
                e.getMessage(),
                Instant.now(),
                status
        );

        return new ResponseEntity<>(postException, status);
    }
}
