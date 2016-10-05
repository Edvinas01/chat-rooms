package com.edd.chat.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class UnhandledError {

    private final String error;

    @JsonCreator
    public UnhandledError(@JsonProperty("error") String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}