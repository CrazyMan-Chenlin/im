package com.roy.im.connection.service;


import com.roy.common.sdk.model.Message;
import com.roy.im.connection.hystrix.MessageServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author chenlin
 */
@FeignClient(value = "im-service", fallback = MessageServiceHystrix.class)
public interface MessageService {

    /**
     * 查询会话消息
     * @param ownerUid
     * @param otherUid
     * @return
     */
    @RequestMapping(value = "/queryMsg",method = RequestMethod.GET)
    List<Message> queryConversationMsg(long ownerUid, long otherUid);

    /**
     * 查询总未读
     * @param unreadOwnerUid
     * @return
     */
    @RequestMapping(value = "/queryUnread",method = RequestMethod.GET)
    long queryTotalUnread(long unreadOwnerUid);

    /**
     *
     * @param senderUid
     * @param recipientUid
     * @param content
     * @param msgType
     * @return
     */
    @RequestMapping(value = "/sendMsg",method = RequestMethod.POST)
    Message sendMessage(long senderUid, long recipientUid, String content, int msgType);
}
