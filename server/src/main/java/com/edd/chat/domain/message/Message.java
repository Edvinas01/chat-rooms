package com.edd.chat.domain.message;

import com.edd.chat.domain.account.Account;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Message {

    @Id
    private String id;

    private Account account;
    private String content;

    private Date createdOn = new Date();

    public Message(Account account, String content) {
        this.account = account;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
}