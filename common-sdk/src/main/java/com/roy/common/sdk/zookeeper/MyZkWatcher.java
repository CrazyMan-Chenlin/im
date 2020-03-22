package com.roy.common.sdk.zookeeper;

import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：ZK服务观察者事件
 * @author chenlin
 */
@Slf4j
public class MyZkWatcher implements Watcher{

    /**
     * 异步锁
     */
    private CountDownLatch cdl;

    /**
     * 标记
     */
    private String mark;
    
    public MyZkWatcher(CountDownLatch cdl,String mark) {
        this.cdl = cdl;
        this.mark = mark;
    }

    /**
     * 监听事件处理方法
     */
    @Override
    public void process(WatchedEvent event) {
        log.info(mark+" watcher监听事件：{}",event);
        cdl.countDown();
    }

}