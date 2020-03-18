package com.roy.account.controller;

import com.roy.account.model.JWT;
import com.roy.account.model.User;
import com.roy.account.model.UserLoginDTO;
import com.roy.account.service.UserService;
import com.roy.common.sdk.annotation.ResponseResult;
import com.roy.common.sdk.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenlin
 */
@RestController
@ResponseResult
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/user/register")
    public Integer addUser(@RequestBody @Validated User user) {
        return userService.insert(user);
    }

    @GetMapping(value = "/user/{uid}")
    public User getUser(@PathVariable("uid") Integer uid) {
        User user = new User();
        user.setUid(uid);
        return userService.select(user).get(0);
    }

    @GetMapping(value = "/test1")
    public String test() {
        return "src/main/test";
    }

    @PutMapping(value = "/user")
    public Integer updateUser(User user) {
        return userService.update(user);
    }

    @DeleteMapping(value = "/user/{uid}")
    public Integer deleteUser(@PathVariable("uid") String uid) {
        //注销用户
        return userService.delete(uid);
    }

    @GetMapping(value = "/test")
    public String test1() {
        return "hahha";
    }


    @PostMapping("/user/login")
    public UserLoginDTO login(User user) {
        return userService.login(user);
    }

    @PostMapping("/user/logout")
    public Result logout(@RequestBody String jti) {
        userService.logout(jti);
        return Result.success();
    }

}
