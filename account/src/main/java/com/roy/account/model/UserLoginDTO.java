package com.roy.account.model;

import lombok.Data;

/**
 * @author chenlin
 */
@Data
public class UserLoginDTO {

    private JWT jwt;
    private Integer uid;
}
