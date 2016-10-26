package com.edd.chat.admin.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountManagementDetails {

    private final boolean enabled;

    @JsonCreator
    public AccountManagementDetails(@JsonProperty("enabled") boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}