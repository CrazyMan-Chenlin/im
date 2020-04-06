package com.roy.im.service;

import com.roy.im.server.ImServerApplication;
import com.roy.im.server.service.PushMsgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ImServerApplication.class)
public class ImServerApplicationTests {

    @Autowired
    PushMsgService pushMsgService;

    @Test
    void contextLoads() {
        pushMsgService.push();
    }

}
