package com.edd.chat.channel;

import com.edd.chat.account.AccountService;
import com.edd.chat.channel.comment.Comment;
import com.edd.chat.exception.ChatException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelService.class);

    private final ChannelRepository channelRepository;
    private final AccountService accountService;

    private final String mainChannel;
    private final long maxComments;

    @Autowired
    public ChannelService(ChannelRepository channelRepository,
                          AccountService accountService,
                          @Value("${chat.channels.main}") String mainChannel,
                          @Value("${chat.max.comments}") long maxComments) {

        this.channelRepository = channelRepository;
        this.accountService = accountService;
        this.mainChannel = mainChannel;
        this.maxComments = maxComments;
    }

    /**
     * Initialize main channel.
     */
    @PostConstruct
    public Channel init() {
        return channelRepository.findByName(mainChannel)
                .orElseGet(() -> createChannel(mainChannel));
    }

    /**
     * Get channel by id.
     *
     * @param id channel id.
     * @return channel.
     */
    public Channel getChannel(String id) {
        return Optional.ofNullable(channelRepository
                .findOne(id))
                .orElseThrow(() -> new ChatException("Channel not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Create channel with unique name.
     *
     * @param name channel name.
     * @return created channel.
     */
    public Channel createChannel(String name) {
        if (StringUtils.isBlank(name)) {
            throw new ChatException("Channel name must not be null", HttpStatus.BAD_REQUEST);
        }

        name = name.trim();
        try {
            LOGGER.debug("Creating new channel with name: {}", name);
            return channelRepository.save(new Channel(name));

        } catch (DuplicateKeyException e) {
            LOGGER.error("Could not channel: {}",
                    e.getMostSpecificCause().getMessage());

            throw new ChatException("Channel with such name already exists", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete channel by id.
     *
     * @param id channel id.
     */
    public void deleteChannel(String id) {
        Channel channel = getChannel(id);
        if (mainChannel.equals(channel.getName())) {
            throw new ChatException("Cannot delete main channel", HttpStatus.BAD_REQUEST);
        }

        LOGGER.debug("Deleting channel with id: {}, name: {}",
                channel.getId(), channel.getName());

        channelRepository.delete(id);
    }

    /**
     * Get all channels.
     *
     * @return list of channels.
     */
    public List<Channel> getChannels() {
        return channelRepository.findAll();
    }

    /**
     * Create a new comment under a channel.
     *
     * @param channelId channel id.
     * @param details   comment message details.
     * @return created comment.
     */
    public Comment comment(String channelId,
                           Comment details) {

        String message = details.getMessage();

        if (StringUtils.isEmpty(message)) {
            throw new ChatException("Comment message must not be blank", HttpStatus.BAD_REQUEST);
        }

        Channel channel = getChannel(channelId);
        Comment comment = new Comment(accountService.getAccount(), message);
        channel.getComments()
                .add(comment);

        LOGGER.debug("Adding comment to channel: {}, message: {}",
                channelId, message);

        // Cleanup old comments.
        if (channel.getComments().size() > maxComments) {
            channel.getComments().remove(0);
        }

        channelRepository.save(channel);
        return comment;
    }

    /**
     * Get channel comments.
     *
     * @param channelId channel id from where to get the comments.
     * @param count     how many comments to get.
     * @return list of comments.
     */
    public List<Comment> getComments(String channelId, long count) {
        if (count > maxComments || count <= 0) {
            throw new ChatException("Count must be positive and not greater than " + maxComments,
                    HttpStatus.BAD_REQUEST);
        }

        List<Comment> comments = getChannel(channelId)
                .getComments();

        return comments
                .stream()
                .sorted(Comparator.comparing(Comment::getSentOn))
                .skip(Math.max(0, comments.size() - count))
                .collect(Collectors.toList());
    }
}