package com.roy.common.sdk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;

/**
 * @author chenlin
 */
@SpringBootApplication
public class CommonSdkApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonSdkApplication.class, args);
    }

}
