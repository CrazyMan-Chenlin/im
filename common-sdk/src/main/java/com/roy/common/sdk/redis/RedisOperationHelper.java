package com.roy.common.sdk.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * redis辅助类
 * @author banma
 */
@Component
public class RedisOperationHelper {
	
	@Autowired
    private RedisTemplate<String,Object> redisTemplate;
 
    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
    
    /**
     * 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        assert keys != null;
        if (keys.size() > 0){
        	redisTemplate.delete(keys);
        }
    }
    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }
    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }
    /**
     * 哈希 获取哈希的key集合
     * @param key
     * @return
     */
    public Set<Object> hmKeys(String key){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.keys(key);        
    }
    /**
     * 哈希 删除哈希的key
     * @param key
     * @param hashKey
     */
    public void hmDelete(String key,String hashKey){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.delete(key, hashKey);       
    }    
    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }
    
    /**
     * 获取所有key值
     * @param key
     * @return
     */
    public Set<Object>  hmKeySet(String key){
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    	return hash.keys(key);	
    }
    
    /**
     * 获取所有key值
     * @param key
     * @return
     */
    public void  hmRemove(String key, Object hashKey){
    	HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    	hash.delete(key, hashKey); 	
    }
 
    /**
     * 列表添加
     * @param k
     * @param v
     */
    public void lPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }
 
    /**
     * 列表获取
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }
 
    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void add(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }

    /**
     * 判断是否包含  sismember
     *
     * @param key
     * @param value
     */
    public Boolean contains(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }
 
    /**
     * 集合获取
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }
 
    /**
     * 集合长度
     * @param key
     * @return
     */
    public Long setSize(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.size(key);
    }
 
    /**
     * 集合获取
     * @param key
     * @param count
     * @return
     */
    public Set<Object> setMembers(String key, int count){
    	 SetOperations<String, Object> set = redisTemplate.opsForSet();
    	 return set.distinctRandomMembers(key, count);
    }
    
    /**
     * 删除集合数据
     * @param key
     * @param value
     */
    public void remove(String key, Object value){
    	 SetOperations<String, Object> set = redisTemplate.opsForSet();
    	 set.remove(key, value);
    }
 
    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key,Object value,double scoure){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
    }
 
    /**
     * 有序集合获取
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }
    
    /**
     * 消息队列实现
     * @param channel
     * @param message
     */
    public void convertAndSend(String channel, Object message){
    	redisTemplate.convertAndSend(channel, message);
    }
    
    /**
     * 数列添加
     * @param key
     * @param value
     */
    public void addList(String key,Object value){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(key, value);
    }
 
    /**
     * 数列获取
     * @param key
     * @return
     */
    public List<Object> getList(String key){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(key, 0, list.size(key));
    }
    
    /**
     * 左弹出数列
     * @param key
     * @return
     */
    public Object popList(String key) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.leftPop(key);
    }
    /**
     * 自增数（计数器）
     * @param k
     * @param l
     * @return
     */
    public Long increment(String k, Long l) {
        return redisTemplate.opsForValue().increment(k, l);
    }

 
}