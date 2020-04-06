package com.roy.im.connection.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 查询总未读数处理器
 * @author chenlin
 */
@Slf4j
@Service
public class QueryUnReadNumsProcessor implements TypeProcessor {

    @Autowired
    private MessageService messageService;

    @Override
    public String handler(JSONObject data, Channel channel) {
        long unreadOwnerUid = data.getLong("uid");
        long totalUnread = messageService.queryTotalUnread(unreadOwnerUid);
        return "{\"type\":5,\"data\":{\"unread\":" + totalUnread + "}}";
    }

    @Override
    public int getType() {
        return 5;
    }
}
