package com.roy.im.connection.service;

import com.alibaba.fastjson.JSONObject;
import com.roy.im.connection.constant.AttributeKeyConstant;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenlin
 */
@Service
@Slf4j
public class AckProcessor implements TypeProcessor {
    @Override
    public String handler(JSONObject data, Channel channel) {
        long tid = data.getLong("tid");
        ConcurrentHashMap<Long, JSONObject> nonAckedMap = channel.attr(AttributeKeyConstant.NON_ACKED_MAP).get();
        nonAckedMap.remove(tid);
        return null;
    }

    @Override
    public int getType() {
        return 6;
    }
}
