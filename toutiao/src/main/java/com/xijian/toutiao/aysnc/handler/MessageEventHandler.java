package com.xijian.toutiao.aysnc.handler;

import com.xijian.toutiao.Service.MessageService;
import com.xijian.toutiao.aysnc.EventHandler;
import com.xijian.toutiao.aysnc.EventModel;
import com.xijian.toutiao.aysnc.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
public class MessageEventHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Override
    public void doHandler(EventModel eventModel) {
        messageService.UpdateReadCount(eventModel.getActorId());
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.Message);
    }
}
