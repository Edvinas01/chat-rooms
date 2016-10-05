package com.edd.chat.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<UnhandledError> handleChatException(ChatException e) {
        return new ResponseEntity<>(new UnhandledError(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<UnhandledError> handleChatException(Exception e) {
        LOGGER.error("Unhandled exception", e);
        return new ResponseEntity<>(new UnhandledError(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}