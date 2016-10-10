package com.edd.chat.channel;

import com.edd.chat.channel.comment.Comment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "channels")
public class Channel {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @Field("comments")
    private List<Comment> comments = new ArrayList<>();

    public Channel(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getName() {
        return name;
    }
}