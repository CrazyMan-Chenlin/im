package com.roy.account;

import com.roy.account.service.AuthServiceClient;
import com.roy.common.sdk.exception.ZkLockException;
import com.roy.common.sdk.redis.RedisOperationHelper;
import com.roy.common.sdk.zookeeper.DLock;
import com.roy.common.sdk.zookeeper.MyZkConnect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/*@SpringBootTest*/
class DemoApplicationTests {

    @Autowired
    AuthServiceClient authServiceClient;

    @Autowired
    RedisOperationHelper redisOperationHelper;

    @Autowired
    MyZkConnect zkConnect;

    @Autowired
    DLock dLock;

    @Test
    void contextLoads() {
        String path = "/test";
        try {
            System.out.println("在拿锁：" + path + System.currentTimeMillis());
            dLock.mutex(path, () -> {
                System.out.println("拿到锁了" + System.currentTimeMillis());
                System.out.println("i am doing...");
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("操作完成了" + System.currentTimeMillis());
            }, 1000, TimeUnit.MILLISECONDS);
        } catch (ZkLockException e) {
            System.out.println("拿不到锁呀" + System.currentTimeMillis());
        }
    }

    @Test
    public void testGetyushu() {
        for (int i = 1; i < 99; i++) {
            System.out.println(i % 3);
        }
    }

    @Test
    public void testSynchronize(){
            synchronized (this) {
                System.out.println("Method 1 start");
            }
    }
}
