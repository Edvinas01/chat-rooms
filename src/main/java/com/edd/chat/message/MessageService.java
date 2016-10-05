package com.edd.chat.message;

import com.edd.chat.account.AccountService;
import com.edd.chat.domain.channel.Channel;
import com.edd.chat.domain.channel.ChannelRepository;
import com.edd.chat.domain.message.Message;
import com.edd.chat.domain.message.MessageRepository;
import com.edd.chat.exception.ChatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    private final AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          ChannelRepository channelRepository,
                          AccountService accountService) {

        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.accountService = accountService;
    }

    public Message create(String channelId, Message details) {
        Channel channel = channelRepository.findOne(channelId);
        if (channel == null) {
            throw new ChatException("Channel not found", HttpStatus.NOT_FOUND);
        }

        Message message = new Message(
                accountService.getAccount(),
                details.getContent());

        channel.getMessages().add(message);
        return message;
    }
}