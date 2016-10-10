package com.edd.chat.channel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ChannelModel {

    private final String id;
    private final String name;

    private final int commentCount;

    @JsonCreator
    private ChannelModel(@JsonProperty("id") String id,
                         @JsonProperty("name") String name,
                         @JsonProperty("commentCount") int commentCount) {

        this.id = id;
        this.name = name;
        this.commentCount = commentCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public static ChannelModel create(Channel channel) {
        return new ChannelModel(channel.getId(),
                channel.getName(),
                channel.getComments().size());
    }
}