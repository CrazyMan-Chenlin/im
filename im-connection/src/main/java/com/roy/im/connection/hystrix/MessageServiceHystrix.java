package com.roy.im.connection.hystrix;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.roy.common.sdk.model.Message;
import com.roy.im.connection.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenlin
 */
@Component
@Slf4j
public class MessageServiceHystrix implements MessageService {

    @Override
    public List<Message> queryConversationMsg(long ownerUid, long otherUid) {
        log.error("Fallback of queryConversationMsg is executed, return a error Message");
        return null;
    }

    @Override
    public long queryTotalUnread(long unreadOwnerUid) {
        return 0;
    }

    @Override
    public Message sendMessage(long senderUid, long recipientUid, String content, int msgType) {
        return null;
    }
}
