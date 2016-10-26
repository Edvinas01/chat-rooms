package com.edd.chat.account;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Document(collection = "accounts")
public class Account implements UserDetails {

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

    private UUID tokenVersion = UUID.randomUUID();

    public Account(String username, String password, String internalUsername) {
        this.username = username;
        this.password = password;
        this.internalUsername = internalUsername;
    }

    public String getId() {
        return id;
    }

    public String getInternalUsername() {
        return internalUsername;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UUID getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(UUID tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role.name());
    }

    @Override
    public String getPassword() {
        return password;
    }
}