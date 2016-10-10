package com.edd.chat.comment.channel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends MongoRepository<Channel, String> {
}