package com.lyz.dao.impl;

import com.lyz.dao.IUserDao;
import com.lyz.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */
public class UserDaoImpl extends AbstractBaseRedisDao<String,User> implements IUserDao{
    public boolean add(final User user) {
        boolean result=redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer=getRedisSerializer();
                byte[] key=serializer.serialize(user.getId());
                byte[] name=serializer.serialize(user.getPassword());
                return redisConnection.setNX(key,name);
            }
        });
        return result;
    }

    public boolean add(List<User> list) {
        return false;
    }

    public void delete(String key) {

    }

    public void delete(List<User> list) {

    }

    public boolean update(User user) {
        return false;
    }

    public User get(String keyId) {
        return null;
    }
}
