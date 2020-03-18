package com.roy.api.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.roy.common.sdk.redis.RedisOperationHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.Socket;
import java.util.*;

/**
 * @author chenlin
 */
@Slf4j
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private RedisOperationHelper redisOperationHelper;

    private static final String LOGIN_TOKEN = "LOGIN_TOKEN";

    @Value("${gatewayLoginUrl}")
    private String gatewayLoginUrl;

    @Value("${gatewayRegisterUrl}")
    private String gatewayRegister;

    /**
     * 返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型
     * pre：路由之前
     * routing：路由之时
     * post： 路由之后
     * error：发送错误调用
     *
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        //排除掉登陆和注册入口
        String requestUri = request.getRequestURI();
        if (gatewayLoginUrl.equals(requestUri) ||
                gatewayRegister.equals(requestUri)){
           return null;
        }
        String jti = request.getParameter("jti");
        if (jti == null || !redisOperationHelper.exists(LOGIN_TOKEN + jti)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(302);
            //返回登陆页面
            ctx.getResponse().sendRedirect("http://www.baidu.com");
        }
        return null;
    }

}
class ThreadA extends  Thread {

    @Override
    public boolean isInterrupted() {
        return super.isInterrupted();
    }

    synchronized public void  aa(){

    }

    @Override
    public void run() {
        interrupt();
        super.run();
    }
}
