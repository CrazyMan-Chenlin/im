package com.roy.im.connection.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

/**
 * 查询总未读数处理器
 * @author chenlin
 */
public class QueryUnReadNumsProcessor implements TypeProcessor {

    @Override
    public String handler(JSONObject data, Channel channel) {
        long senderUid = data.getLong("senderUid");
        long recipientUid = data.getLong("recipientUid");
        String content = data.getString("content");
        int msgType = data.getIntValue("msgType");
        //todo 远程接口查询
        int totalUnread = 0;
        return "{\"type\":5,\"data\":{\"unread\":" + totalUnread + "}}";
    }

    @Override
    public int getType() {
        return 5;
    }
}
