package com.roy.auth.server;

import com.roy.auth.server.dao.UserDao;
import com.roy.auth.server.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServerApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void contextLoads() {
        User chenlin = userDao.selectByUsername("roy");
        System.out.println(chenlin);
    }

}
