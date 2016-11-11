package com.edd.chat.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Jwt model with some additional details.
 */
public final class TokenDetails {

    private final String token;
    private final Date expires;

    @JsonCreator
    public TokenDetails(@JsonProperty("token") String token,
                        @JsonProperty("expires") Date expires) {

        this.token = token;
        this.expires = new Date(expires.getTime());
    }

    public String getToken() {
        return token;
    }

    public Date getExpires() {
        return new Date(expires.getTime());
    }
}