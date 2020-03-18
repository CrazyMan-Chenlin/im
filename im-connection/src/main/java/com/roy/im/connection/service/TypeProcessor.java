package com.roy.im.connection.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

/**
 * 类型处理器
 * @author chenlin
 */
public interface TypeProcessor {


    /**
     * 对类型进行处理
     * @param data
     * @param channel
     * @return
     */
    String handler(JSONObject data, Channel channel);

    int getType();
}
