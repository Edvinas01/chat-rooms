package com.edd.chat.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ChatException.class)
    public ResponseEntity<Error> handleChatException(ChatException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), e.getStatus());
    }

    private static final class Error {

        private final String error;

        private Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}