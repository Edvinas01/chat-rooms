package com.edd.chat.token;

import java.util.UUID;

public final class DecodedToken {

    private final String username;
    private final UUID version;

    public DecodedToken(String username,
                        UUID version) {

        this.username = username;
        this.version = version;
    }

    /**
     * Get decoded internal username.
     *
     * @return internal username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get decoded token version.
     *
     * @return token version.
     */
    public UUID getVersion() {
        return version;
    }
}