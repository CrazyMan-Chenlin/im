package com.roy.im.connection.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import io.netty.channel.Channel;

/**
 * 发送消息处理器
 * @author chenlin
 */
public class SendMessageProcessor implements TypeProcessor {

    @Override
    public String handler(JSONObject data, Channel channel) {
        long senderUid = data.getLong("senderUid");
        long recipientUid = data.getLong("recipientUid");
        String content = data.getString("content");
        int msgType = data.getIntValue("msgType");
        //todo 调用远程服务发送消息
        return null;
    }

    @Override
    public int getType() {
        return 3;
    }
}
