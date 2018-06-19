package com.lyz.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by Administrator on 2018/6/11.
 */
public abstract class AbstractBaseRedisDao<K,V> {
    @Autowired
    protected RedisTemplate<K,V>  redisTemplate;
    public void setRedisTemplate(RedisTemplate<K,V> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    /**
     * get RedisSerializer
     * @return
     */
    protected RedisSerializer<String> getRedisSerializer(){
        return redisTemplate.getStringSerializer();
    }
}
