package com.roy.account.hystrix;

import com.roy.account.model.JWT;
import com.roy.account.service.AuthServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author chenlin
 */
@Component
@Slf4j
public class AuthServiceHystrix implements AuthServiceClient {

    @Override
    public JWT getToken(String authorization, String type, String username, String password) {
        log.warn("Fallback of getToken is executed");
        return null;
    }

    @Override
    public JWT refreshToken(String type, String refreshToken, String clientId, String clientSecret) {
        log.warn("refreshToken failed");
        return null;
    }

}

