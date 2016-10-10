package com.edd.chat.comment;

import com.edd.chat.account.AccountService;
import com.edd.chat.comment.channel.Channel;
import com.edd.chat.comment.channel.ChannelRepository;
import com.edd.chat.exception.ChatException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);
    public static final String GENERAL_CHANNEL = "General";

    private final ChannelRepository channelRepository;
    private final AccountService accountService;

    @Autowired
    public CommentService(ChannelRepository channelRepository,
                          AccountService accountService) {

        this.channelRepository = channelRepository;
        this.accountService = accountService;
    }

    @PostConstruct
    public void init() {

    }

    /**
     * Create a new comment under a channel.
     *
     * @param channelId channel id.
     * @param message   comment message.
     * @return created comment.
     */
    public Comment comment(String channelId,
                           String message) {

        if (StringUtils.isEmpty(message)) {
            throw new ChatException("Comment message must not be blank", HttpStatus.BAD_REQUEST);
        }

        Channel channel = Optional.ofNullable(channelRepository
                .findOne(channelId))
                .orElseThrow(() -> new ChatException("Channel not found", HttpStatus.NOT_FOUND));

        Comment comment = new Comment(accountService.getAccount(), message);
        channel.getComments()
                .add(comment);

        LOGGER.debug("Adding comment to channel: {}, message: {}",
                channelId, message);

        channelRepository.save(channel);
        return comment;
    }

    /**
     * Create channel with unique name.
     *
     * @param name channel name.
     * @return created channel.
     */
    public Channel create(String name) {
        if (StringUtils.isBlank(name)) {
            throw new ChatException("Channel name must not be null", HttpStatus.BAD_REQUEST);
        }
        return channelRepository.save(new Channel(name.trim()));
    }
}