package com.roy.im.connection.constant;

import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenlin
 */
public class AttributeKeyConstant {

    /**
     * 这个是用来存储用户上线后，发送的消息编号,消息编号可以用来客户端排序等等
     */
    public static final AttributeKey<AtomicLong> TID_GENERATOR = AttributeKey.valueOf("tid_generator");
    /**
     * 用来存储推送失败列表
     */
    public static final AttributeKey<ConcurrentHashMap> NON_ACKED_MAP = AttributeKey.valueOf("non_acked_map");
}
