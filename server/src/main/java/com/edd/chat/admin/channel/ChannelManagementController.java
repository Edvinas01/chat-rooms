package com.edd.chat.admin.channel;

import com.edd.chat.channel.ChannelModel;
import com.edd.chat.channel.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/admin/channels")
public class ChannelManagementController {

    private final ChannelService channelService;

    @Autowired
    public ChannelManagementController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public ChannelModel createChannel(@RequestBody ChannelModel channel) {
        return ChannelModel.create(channelService
                .createChannel(channel.getName()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteChannel(@PathVariable String id) {
        channelService.deleteChannel(id);
    }
}