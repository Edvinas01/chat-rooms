package com.edd.chat.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }
}