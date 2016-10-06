package com.edd.chat.exception;

import org.springframework.http.HttpStatus;

public class ChatException extends RuntimeException {

    private final HttpStatus code;

    public ChatException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

    public HttpStatus getStatus() {
        return code;
    }
}