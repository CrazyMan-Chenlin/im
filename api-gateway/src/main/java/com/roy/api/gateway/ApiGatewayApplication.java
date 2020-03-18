package com.roy.api.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenlin
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.roy.*")
@RestController
@RefreshScope
public class ApiGatewayApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Value("${gatewayLoginUrl}")
    private String hello;

    @RequestMapping("/hello")
    public String hello() {
        return hello;
    }

}
