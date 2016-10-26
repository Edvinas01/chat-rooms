package com.edd.chat.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ErrorMessage {

    private final String error;

    @JsonCreator
    public ErrorMessage(@JsonProperty("error") String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}