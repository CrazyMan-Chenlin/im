package com.roy.im.connection.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳处理器
 * @author chenlin
 */
@Slf4j
public class HeartBeatProcessor implements TypeProcessor{

    @Override
    public String handler(JSONObject data, Channel channel) {
        long uid = data.getLong("uid");
        long timeout = data.getLong("timeout");
        log.info("[heartbeat]: uid = {} , current timeout is {} ms, channel = {}", uid, timeout, channel);
        return "{\"type\":1,\"status\":\"success\"}";
    }

    @Override
    public int getType() {
        return 0;
    }

}
