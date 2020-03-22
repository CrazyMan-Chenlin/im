package com.roy.common.sdk.zookeeper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author chenlin
 */
@Slf4j
@Data
public class CuratorFactoryBean implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {

    private String connectionString;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private RetryPolicy retryPolicy;
    private CuratorFramework client;

    public CuratorFactoryBean(String connectionString) {
        this(connectionString, 500, 500);
    }


    public CuratorFactoryBean(String connectionString, int sessionTimeoutMs, int connectionTimeoutMs) {
        this.connectionString = connectionString;
        this.sessionTimeoutMs = sessionTimeoutMs;
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    @Override
    public void destroy() throws Exception {
        log.info("Closing curator framework...");
        this.client.close();
        log.info("Closed curator framework.");
    }

    @Override
    public CuratorFramework getObject() throws Exception {
        return this.client;
    }

    @Override
    public Class<?> getObjectType() {
        return this.client != null ? this.client.getClass() : CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(this.connectionString)) {
            throw new IllegalStateException("connectionString can not be empty.");
        } else {
            if (this.retryPolicy == null) {
                this.retryPolicy = new ExponentialBackoffRetry(1000, 2147483647, 180000);
            }

            this.client = CuratorFrameworkFactory.newClient(this.connectionString, this.sessionTimeoutMs, this.connectionTimeoutMs, this.retryPolicy);
            this.client.start();
            this.client.blockUntilConnected(30, TimeUnit.MILLISECONDS);
        }
    }

}
