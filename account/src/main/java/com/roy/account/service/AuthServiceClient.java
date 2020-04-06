package com.roy.account.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.roy.account.hystrix.AuthServiceHystrix;
import com.roy.account.model.JWT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * @author chenlin
 */

@FeignClient(value = "auth-service", fallback = AuthServiceHystrix.class)
public interface AuthServiceClient {

    @PostMapping("/oauth/token")
    @HystrixCommand(commandProperties={
           @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds" , value = "3000")//超时时间
   })
    JWT getToken(@RequestHeader("Authorization") String authorization,
                 @RequestParam("grant_type") String type,
                 @RequestParam("username") String username,
                 @RequestParam("password") String password);

    @PostMapping("/oauth/token")
    JWT refreshToken( @RequestParam("grant_type") String type,
                  @RequestParam("refresh_token") String refreshToken,
                  @RequestParam("client_id")String clientId,
                  @RequestParam("client_secret")String clientSecret);
}

