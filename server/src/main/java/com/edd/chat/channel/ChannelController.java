package com.edd.chat.channel;

import com.edd.chat.channel.comment.CommentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/channels")
public class ChannelController {

    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelModel> getChannels() {
        return channelService.getChannels()
                .stream()
                .map(ChannelModel::create)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public List<CommentModel> getComments(@PathVariable String id,
                                          @RequestParam(defaultValue = "100") int count) {

        return channelService.getComments(id, count)
                .stream()
                .map(CommentModel::create)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST)
    public CommentModel comment(@PathVariable String id,
                                @RequestBody CommentModel comment) {

        return CommentModel.create(channelService
                .comment(id, comment.toComment()));
    }
}