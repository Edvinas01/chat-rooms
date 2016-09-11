package com.edd.chat.domain.user;

import com.edd.chat.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class ChatUser extends BaseEntity implements UserDetails {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Date registered = new Date();

    @JsonManagedReference
    @OneToMany(mappedBy = "chatUser")
    private List<AuthMethod> authMethods = new ArrayList<>();

    private ChatUser() {
    }

    public ChatUser(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return "N/A";
    }

    public Date getRegistered() {
        return registered;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<AuthMethod> getAuthMethods() {
        return authMethods;
    }
}