package com.edd.chat.domain.channel;

import com.edd.chat.domain.message.Message;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Channel {

    @Id
    private String id;

    @DBRef
    private List<Message> messages = new ArrayList<>();

    public Channel() {
    }

    public String getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }
}