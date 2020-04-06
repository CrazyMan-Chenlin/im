package com.roy.im.connection.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roy.common.sdk.model.Message;
import com.sun.org.apache.regexp.internal.RE;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发送消息处理器
 * @author chenlin
 */
@Slf4j
@Service
public class SendMessageProcessor implements TypeProcessor {

    @Autowired
    MessageService messageService;

    @Override
    public String handler(JSONObject data, Channel channel) {
        long senderUid = data.getLong("senderUid");
        long recipientUid = data.getLong("recipientUid");
        String content = data.getString("content");
        int msgType = data.getIntValue("msgType");
        Message message = messageService.sendMessage(senderUid, recipientUid, content, msgType);
        String msg = "";
        if (message != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", 3);
            jsonObject.put("data", JSONObject.toJSON(message));
            msg = jsonObject.toJSONString();
        }
        return msg;
    }

    @Override
    public int getType() {
        return 3;
    }
}
