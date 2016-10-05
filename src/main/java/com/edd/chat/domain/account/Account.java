package com.edd.chat.domain.account;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Account {

    public enum Role {
        ROLE_USER,
        ROLE_ADMINISTRATOR
    }

    @Id
    private String id;

    private String username;
    private String password;

    @Indexed(unique = true)
    private String internalUsername;

    private boolean enabled;
    private Role role;

    public Account(String username, String password, String internalUsername) {
        this.username = username;
        this.password = password;
        this.internalUsername = internalUsername;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInternalUsername() {
        return internalUsername;
    }

    public void setInternalUsername(String internalUsername) {
        this.internalUsername = internalUsername;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}