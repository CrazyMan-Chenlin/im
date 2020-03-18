package com.roy.im.connection.service;


import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

/**
 * 查询消息处理器
 * @author chenlin
 */
public class QueryMessageProcessor implements TypeProcessor {
    @Override
    public String handler(JSONObject data, Channel channel) {
        long ownerUid = data.getLong("ownerUid");
        long otherUid = data.getLong("otherUid");
        //todo 调用im-service的业务逻辑模块
        return null;
    }

    @Override
    public int getType() {
        return 2;
    }
}
