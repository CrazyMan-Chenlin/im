package com.roy.im.connection.service;

import com.alibaba.fastjson.JSONObject;
import com.roy.im.connection.constant.AttributeKeyConstant;
import com.roy.im.connection.handler.WebsocketRouterHandler;
import com.roy.im.connection.pool.EnhancedThreadFactory;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenlin
 */
@Component
@Slf4j
public class PushService {

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(50, new EnhancedThreadFactory("ackCheckingThreadPool"));

    @KafkaListener(topics = {"sendMsg"}, groupId = "sendMsgConsumer")
    public void pushMsg(ConsumerRecord<String, String> record) {
        log.info("groupId = sendMsgConsumer, message = " + record.toString());
        JSONObject msgJson = JSONObject.parseObject(record.value());
        long otherUid = msgJson.getLong("otherUid");
        JSONObject pushJson = new JSONObject();
        pushJson.put("type", 4);
        pushJson.put("data", msgJson);
        Channel channel = WebsocketRouterHandler.userChannel.get(otherUid);
        if (channel != null && channel.isActive() && channel.isWritable()) {
            AtomicLong generator = channel.attr(AttributeKeyConstant.TID_GENERATOR).get();
            long tid = generator.incrementAndGet();
            pushJson.put("tid", tid);
            channel.writeAndFlush(new TextWebSocketFrame(pushJson.toJSONString())).addListener(future -> {
                if (future.isCancelled()) {
                    log.warn("future has been cancelled. {}, channel: {}", pushJson, channel);
                } else if (future.isSuccess()) {
                    addMsgToAckBuffer(channel, pushJson);
                    log.warn("future has been successfully pushed. {}, channel: {}", pushJson, channel);
                } else {
                    log.error("message write fail, {}, channel: {}", pushJson, channel, future.cause());
                }
            });
        }
    }
    /**
     * 将推送的消息加入待ack列表
     * @param channel
     * @param msgJson
     */
     public void addMsgToAckBuffer(Channel channel, JSONObject msgJson) {
        channel.attr(AttributeKeyConstant.NON_ACKED_MAP).get().put(msgJson.getLong("tid"), msgJson);
        //5s后检查一次
        executorService.schedule(() -> {
            if (channel.isActive()) {
                //检查是否重推
                checkAndResend(channel, msgJson);
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }

    /**
     * 检查并重推
     * @param channel
     * @param msgJson
     */
    private void checkAndResend(Channel channel, JSONObject msgJson) {
        long tid = msgJson.getLong("tid");
        //重推2次
        int tryTimes = 2;
        while (tryTimes > 0) {
            if (channel.attr(AttributeKeyConstant.NON_ACKED_MAP).get().containsKey(tid)) {
                channel.writeAndFlush(new TextWebSocketFrame(msgJson.toJSONString()));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.error("中断异常",e);
                }
            }
            tryTimes--;
        }
    }
}
