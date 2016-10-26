package com.edd.chat.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class AccountModel {

    private final String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String password;
    private final String id;

    private final Account.Role role;

    private final boolean enabled;

    @JsonCreator
    private AccountModel(@JsonProperty("username") String username,
                         @JsonProperty("password") String password,
                         @JsonProperty("id") String id,
                         @JsonProperty("role") Account.Role role,
                         @JsonProperty("enabled") boolean enabled) {

        this.username = username;
        this.password = password;
        this.id = id;
        this.role = role;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Account.Role getRole() {
        return role;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Account toAccount() {
        Account account = new Account(username, password, null);
        account.setRole(role);
        account.setEnabled(enabled);
        return account;
    }

    public static AccountModel create(Account account) {
        return new AccountModel(account.getUsername(),
                account.getPassword(),
                account.getId(),
                account.getRole(),
                account.isEnabled());
    }
}