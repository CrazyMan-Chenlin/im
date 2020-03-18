package com.roy.account.dao;
import java.util.List;
import com.roy.account.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chenlin
 */
@Mapper
public interface UserDao {

    int insert(@Param("pojo") User pojo);

    int insertList(@Param("pojos") List< User> pojo);

    List<User> select(@Param("pojo") User pojo);

    int update(@Param("pojo") User pojo);

    User selectByUsernameOrEmail(@Param("username") String username,@Param("email") String email);

    int delete(@Param("uid") String uid);

}
