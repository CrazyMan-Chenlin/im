package com.roy.im.connection.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 * config for Netty Server
 * @author chenlin
 */
@Component
@ConfigurationProperties(prefix = "websocket.connector.server")
@Data
public class ServerConfig {

    public int port;
    public int portForDr;
    public boolean useEpoll;
    public boolean useMemPool;
    public boolean useDirectBuffer;
    public int bossThreads;
    public int workerThreads;
    public int userThreads;
    public int connTimeoutMills;
    public int soLinger;
    public int backlog;
    public boolean reuseAddr;
    public int sendBuff;
    public int recvBuff;
    public int readIdleSecond;
    public int writeIdleSecond;
    public int allIdleSecond;
    public int idleTimes;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigOfServer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}