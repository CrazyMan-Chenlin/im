package com.roy.im.connection.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.roy.im.connection.factory.TypeProcessorFactory;
import com.roy.im.connection.pool.EnhancedThreadFactory;
import com.roy.im.connection.service.HeartBeatProcessor;
import com.roy.im.connection.service.TypeProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务端处理所有接收消息的handler，这里只是示例，没有拆分太细，建议实际项目中按消息类型拆分到不同的handler中。
 *
 * @author chenlin
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class WebsocketRouterHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    public static final ConcurrentHashMap<Long, Channel> userChannel = new ConcurrentHashMap<>(15000);
    public static final ConcurrentHashMap<Channel, Long> channelUser = new ConcurrentHashMap<>(15000);

    @Autowired
    TypeProcessorFactory typeProcessorFactory;

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(50, new EnhancedThreadFactory("ackCheckingThreadPool"));

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {

            if (frame instanceof TextWebSocketFrame) {
                String msg = ((TextWebSocketFrame) frame).text();
                JSONObject msgJson = JSONObject.parseObject(msg);
                int type = msgJson.getIntValue("type");
                JSONObject data = msgJson.getJSONObject("data");
                TypeProcessor typeProcessor = typeProcessorFactory.getTypeProcessor(type);
                String result;
                if (typeProcessor != null){
                    result = typeProcessor.handler(data, ctx.channel());
                }else{
                    result = "{\"type\":" + type + ",\"data\":{unknown type!}}";
                }
                ctx.writeAndFlush(new TextWebSocketFrame(result));
            }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("[channelActive]:remote address is {} ", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[channelClosed]:remote address is {} ", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("process error. uid is {},  channel info {}", channelUser.get(ctx.channel()), ctx.channel(), cause);
        ctx.channel().close();
    }

    /**
     * 清除用户和socket映射的相关信息
     *
     * @param channel
     */
    public void cleanUserChannel(Channel channel) {
        long uid = channelUser.remove(channel);
        userChannel.remove(uid);
        log.info("[cleanChannel]:remove uid & channel info from gateway, uid is {}, channel is {}", uid, channel);
    }

    //todo 发送消息的推送
}
