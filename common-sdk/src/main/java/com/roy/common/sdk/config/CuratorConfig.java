package com.roy.common.sdk.config;

import com.roy.common.sdk.zookeeper.CuratorFactoryBean;
import com.roy.common.sdk.zookeeper.DLock;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenlin
 */
@Configuration
public class CuratorConfig {

    @Value("${zookeeper.cluster.address}")
    public String zkServerClusterConnect;

    @Value("${zookeeper.sessionTimeoutMs:500}")
    private int sessionTimeoutMs;

    @Value("${zookeeper.connectionTimeoutMs:500}")
    private int connectionTimeoutMs;

    @Value("${zookeeper.dLockRoot:/dLock}")
    private String dLockRoot;

    @Bean
    public CuratorFactoryBean curatorFactoryBean() {
        return new CuratorFactoryBean(zkServerClusterConnect, sessionTimeoutMs, connectionTimeoutMs);
    }

    @Bean
    public DLock dLock(CuratorFramework client) {
        return new DLock(dLockRoot, client);
    }

}
