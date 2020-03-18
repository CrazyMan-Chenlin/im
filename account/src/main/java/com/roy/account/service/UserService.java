package com.roy.account.service;

import com.roy.account.dao.UserDao;
import com.roy.account.model.JWT;
import com.roy.account.model.User;
import com.roy.account.model.UserLoginDTO;
import com.roy.account.util.BPwdEncoderUtil;
import com.roy.common.sdk.enums.ResultCode;
import com.roy.common.sdk.exception.ErrorResult;
import com.roy.common.sdk.redis.RedisOperationHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenlin
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Autowired
    private AuthServiceClient client;

    @Autowired
    private RedisOperationHelper redisOperationHelper;

    private static final String LOGIN_TOKEN = "LOGIN_TOKEN";

    private static final String ONLINE_LIST = "ONLINE_LIST";

   /* public Optional<User> selectUserByUsername(String username){
        User user = userDao.selectByUsernameOrEmail(username, null);
        return Optional.of(user);
    }*/

    public int insert(User user) {
        //先判断是否用重复的邮箱或者用户名
        User userTemp = userDao.selectByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userTemp != null) {
            if (userTemp.getUsername().equals(user.getUsername())) {
                throw new ErrorResult(ResultCode.USERNAME_EXIST);
            }
            if (userTemp.getEmail().equals(user.getEmail())) {
                throw new ErrorResult(ResultCode.EMAIL_EXIST);
            }
        }
        String salt = RandomStringUtils.randomAlphanumeric(4);
        //生成4位salt
        user.setSalt(salt);
        //设置默认头像
        if (StringUtils.isBlank(user.getAvatar())) {
            user.setAvatar("http://cdn.duitang.com/uploads/item" +
                    "/201309/26/20130926095128_SiPMh.jpeg");
        }
  /*      //加盐散列存储
        user.setPassword(Arrays.toString(DigestUtils.md5Digest((salt + user.getPassword()).getBytes())));*/
        user.setPassword(BPwdEncoderUtil.BCryptPassword(user.getPassword()));
        return userDao.insert(user);
    }

    public int insertList(List<User> pojos) {
        return userDao.insertList(pojos);
    }

    public List<User> select(User user) {
        return userDao.select(user);
    }

    public int update(User user) {
        //只能修改密码和头像
        user.setEmail(null);
        user.setUsername(null);
        user.setSalt(null);
        return userDao.update(user);
    }

    public int delete(String uid) {
        return userDao.delete(uid);
    }

    public UserLoginDTO login(User user) {

        // 查询数据库
        User userTemp = userDao.selectByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userTemp == null) {
            throw new ErrorResult(ResultCode.USER_NOT_EXIST);
        }

        if (!BPwdEncoderUtil.matches(user.getPassword(), userTemp.getPassword())) {
            throw new ErrorResult(ResultCode.PASSWORD_ERROR);
        }

        // 从auth-service获取JWT
        JWT jwt = client.getToken("Basic dXNlci1zZXJ2aWNlOjEyMzQ1Ng==", "password", user.getUsername(), user.getPassword());
        if (jwt == null) {
            throw new ErrorResult(ResultCode.LOGIN_ERROR);
        }
        //用redis存储token
        redisOperationHelper.set(LOGIN_TOKEN + jwt.getJti(), "Bearer " + jwt.getAccess_token(),60*60L);
        //存储用户在线列表
        redisOperationHelper.add(ONLINE_LIST,userTemp.getUid());
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setJwt(jwt);
        userLoginDTO.setUid(userTemp.getUid());
        return userLoginDTO;
    }

    public void logout(String jti) {
        //删除对应的缓存列表
        redisOperationHelper.remove(LOGIN_TOKEN + jti);
        //删除在线用户列表
        redisOperationHelper.remove(ONLINE_LIST,jti);
    }
}
