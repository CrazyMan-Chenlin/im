package com.roy.api.gateway;

import com.roy.common.sdk.redis.RedisOperationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class ApiGatewayApplicationTests {

    @Autowired
    RedisOperationHelper redisOperationHelper;

    @Test
    void contextLoads() {
        Set<Object> login_token = redisOperationHelper.setMembers("LOGIN_TOKEN");
        System.out.println(login_token);
    }

}
