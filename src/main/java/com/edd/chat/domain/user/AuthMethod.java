package com.edd.chat.domain.user;

import com.edd.chat.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_IDENTIFIER_TYPE", columnNames = {"identifier", "type"})
})
public class AuthMethod extends BaseEntity {

    @JsonBackReference
    @ManyToOne(optional = false)
    private ChatUser chatUser;

    @Column(nullable = false)
    private String identifier;

    @Column(nullable = false)
    private String type;

    private AuthMethod() {
    }

    public AuthMethod(ChatUser chatUser, String identifier, String type) {
        this.chatUser = chatUser;
        this.identifier = identifier;
        this.type = type;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }
}