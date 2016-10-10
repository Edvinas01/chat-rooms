package com.edd.chat.channel;

import com.edd.chat.account.Account;
import com.edd.chat.account.AccountService;
import com.edd.chat.channel.comment.Comment;
import com.edd.chat.exception.ChatException;
import com.edd.chat.test.AccountFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelServiceTest {

    private static final String MAIN_CHANNEL_ID = "1";
    private static final String MAIN_CHANNEL = "MAIN";
    private static final int MAX_COMMENTS = 2;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private AccountService accountService;

    private Account account = AccountFactory.create();
    private ChannelService service;
    private Comment comment;
    private Channel channel;

    @Before
    public void setUp() {
        service = new ChannelService(channelRepository,
                accountService,
                MAIN_CHANNEL,
                MAX_COMMENTS);

        comment = new Comment(account, "test");
        channel = new Channel(MAIN_CHANNEL);

        when(channelRepository.findOne(MAIN_CHANNEL_ID))
                .thenReturn(channel);

        when(channelRepository.save(any(Channel.class)))
                .then(returnsFirstArg());

        when(accountService.getAccount())
                .thenReturn(account);
    }

    @Test
    public void getChannel() {
        assertThat(service.getChannel(MAIN_CHANNEL_ID)).isEqualTo(channel);
    }

    @Test(expected = ChatException.class)
    public void getChannelNotFound() {
        service.getChannel(MAIN_CHANNEL_ID + "a");
    }

    @Test
    public void createChannel() {
        String name = "test";

        Channel channel = service.createChannel(name);
        assertThat(channel.getName()).isEqualTo(name);
    }

    @Test(expected = ChatException.class)
    public void createChannelInvalidName() {
        service.createChannel("");
    }

    @Test(expected = ChatException.class)
    public void createChannelAlreadyExists() {
        when(channelRepository.save(any(Channel.class)))
                .thenThrow(new DuplicateKeyException(""));

        service.createChannel(MAIN_CHANNEL);
    }

    @Test
    public void getChannels() {
        when(channelRepository.findAll())
                .thenReturn(Collections.singletonList(channel));

        assertThat(service.getChannels()).contains(channel);
    }

    @Test
    public void comment() {
        service.comment(MAIN_CHANNEL_ID, comment);

        List<Comment> comments = channel.getComments();
        assertThat(comments).extracting(Comment::getMessage).containsExactly(comment.getMessage());
        assertThat(comments).extracting(Comment::getAccount).containsExactly(account);
        assertThat(comments).extracting(Comment::getSentOn).isNotNull();
    }

    @Test
    public void commentExceedMax() {
        for (int i = 0; i < MAX_COMMENTS + 1; i++) {
            service.comment(MAIN_CHANNEL_ID, comment);
        }

        assertThat(channel.getComments().size())
                .isEqualTo(MAX_COMMENTS);
    }

    @Test(expected = ChatException.class)
    public void commentInvalidMessage() {
        service.comment(MAIN_CHANNEL_ID, new Comment(account, ""));
    }

    @Test
    public void getComments() {
        for (int i = 0; i < MAX_COMMENTS; i++) {
            service.comment(MAIN_CHANNEL_ID, comment);
        }

        int count = MAX_COMMENTS - 1;
        List<Comment> comments = service.getComments(MAIN_CHANNEL_ID, count);

        assertThat(comments.size()).isEqualTo(count);
    }

    @Test
    public void getCommentsInvalidCounts() {
        assertThatThrownBy(() -> service.getComments(MAIN_CHANNEL_ID, 0))
                .isExactlyInstanceOf(ChatException.class);

        assertThatThrownBy(() -> service.getComments(MAIN_CHANNEL_ID, MAX_COMMENTS + 1))
                .isExactlyInstanceOf(ChatException.class);
    }
}