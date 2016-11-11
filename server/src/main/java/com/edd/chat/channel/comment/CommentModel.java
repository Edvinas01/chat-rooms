package com.edd.chat.channel.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public final class CommentModel {

    private final String username;
    private final String message;
    private final Date sentOn;

    @JsonCreator
    private CommentModel(@JsonProperty("username") String username,
                         @JsonProperty("message") String message,
                         @JsonProperty("sentOn") Date sentOn) {

        this.username = username;
        this.message = message;
        this.sentOn = sentOn;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public Comment toComment() {
        return new Comment(null, message);
    }

    public Date getSentOn() {
        return new Date(sentOn.getTime());
    }

    public static CommentModel create(Comment comment) {
        return new CommentModel(
                comment.getAccount().getUsername(),
                comment.getMessage(),
                comment.getSentOn());
    }
}