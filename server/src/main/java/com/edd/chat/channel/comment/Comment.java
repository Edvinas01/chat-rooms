package com.edd.chat.channel.comment;

import com.edd.chat.account.Account;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

public class Comment {

    private String message;
    private Date sentOn = new Date();

    @DBRef
    private Account account;

    public Comment(Account account, String message) {
        this.account = account;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Date getSentOn() {
        return sentOn;
    }

    public Account getAccount() {
        return account;
    }
}