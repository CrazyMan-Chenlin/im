package com.roy.im.connection;

import com.roy.im.connection.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author chenlin
 */
@SpringBootApplication
@EnableEurekaClient
@EnableAsync
@EnableFeignClients
public class ImConnectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImConnectionApplication.class, args);
    }

}
