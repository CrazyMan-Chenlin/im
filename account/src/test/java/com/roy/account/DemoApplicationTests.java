package com.roy.account;

import com.roy.account.service.AuthServiceClient;
import com.roy.common.sdk.redis.RedisOperationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    AuthServiceClient authServiceClient;

    @Autowired
    RedisOperationHelper redisOperationHelper;

    @Test
    void contextLoads() {
        redisOperationHelper.set("mzr","chenlin");
        System.out.println(redisOperationHelper.get("mzr"));
    }

}
