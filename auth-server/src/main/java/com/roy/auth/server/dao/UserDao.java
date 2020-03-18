package com.roy.auth.server.dao;

import com.roy.auth.server.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenlin
 */
@Mapper
public interface UserDao {

    User selectByUsername(@Param("username") String username);


}
