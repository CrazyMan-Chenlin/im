package com.roy.account.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author chenlin
 */
@Data
public class User {

    private Integer uid;

    @NotBlank(message = "账号不能为空！")
    @Pattern(regexp = "^\\w+$",message = "账号只能填写数字,字符,下划线！")
    private String username;
    @NotBlank(message = "密码不能为空！")
    @Pattern(regexp = "^\\S{6,}$",message = "密码不能含空白符或者小于6位数！")
    private String password;
    private String salt;
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "email不能空！")
    private String email;
    private String avatar;
}
