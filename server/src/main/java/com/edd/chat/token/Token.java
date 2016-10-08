package com.edd.chat.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Jwt model with some additional details.
 */
public final class Token {

    private final String token;
    private final Date expires;

    @JsonCreator
    public Token(@JsonProperty("token") String token,
                 @JsonProperty("expires") Date expires) {

        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public Date getExpires() {
        return expires;
    }
}