package com.edd.chat.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorMessage> handleChatException(ChatException e) {
        LOGGER.warn("Chat exception: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(value = ServletRequestBindingException.class)
    public ResponseEntity<ErrorMessage> handleBindingException(ServletRequestBindingException e) {
        LOGGER.warn("Binding exception: {}", e.getMessage());
        return getError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        LOGGER.error("Unhandled exception: {}", e);
        return getError(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Construct error message response.
     */
    private ResponseEntity<ErrorMessage> getError(Exception e,
                                                  HttpStatus status) {

        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), status);
    }
}