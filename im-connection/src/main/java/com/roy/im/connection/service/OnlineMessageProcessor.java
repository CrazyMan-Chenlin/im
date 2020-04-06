package com.roy.im.connection.service;

import com.alibaba.fastjson.JSONObject;
import com.roy.im.connection.constant.AttributeKeyConstant;
import com.roy.im.connection.handler.WebsocketRouterHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenlin
 */
@Slf4j
@Service
public class OnlineMessageProcessor implements TypeProcessor {

    @Override
    public String handler(JSONObject data, Channel channel) {
        long loginUid = data.getLong("uid");
        WebsocketRouterHandler.userChannel.put(loginUid, channel);
        WebsocketRouterHandler.channelUser.put(channel, loginUid);
        channel.attr(AttributeKeyConstant.TID_GENERATOR).set(new AtomicLong(0));
        channel.attr(AttributeKeyConstant.NON_ACKED_MAP).set(new ConcurrentHashMap<Long, JSONObject>(32));
        log.info("[user bind]: uid = {} , channel = {}", loginUid, channel);
        return "{\"type\":1,\"status\":\"success\"}";
    }

    @Override
    public int getType() {
        return 1;
    }
}
