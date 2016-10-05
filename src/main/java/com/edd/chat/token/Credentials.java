package com.edd.chat.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication credentials.
 */
public final class Credentials {

    private final String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String password;

    @JsonCreator
    public Credentials(@JsonProperty("username") String username,
                       @JsonProperty("password") String password) {

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}