package com.roy.im.connection.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.roy.common.sdk.model.Message;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询消息处理器
 * @author chenlin
 */
@Service
@Slf4j
public class QueryMessageProcessor implements TypeProcessor {

    @Autowired
    private MessageService messageService;

    @Override
    public String handler(JSONObject data, Channel channel) {
        long ownerUid = data.getLong("ownerUid");
        long otherUid = data.getLong("otherUid");
        List<Message> messageList = messageService.queryConversationMsg(ownerUid, otherUid);
        String msgs = "";
        if (messageList != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", 2);
            jsonObject.put("data", JSONArray.toJSON(messageList));
            msgs = jsonObject.toJSONString();
        }
        return msgs;
    }

    @Override
    public int getType() {
        return 2;
    }
}
