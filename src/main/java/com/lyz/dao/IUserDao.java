package com.lyz.dao;

import com.lyz.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */
public interface IUserDao {
    boolean add(User user);
    boolean add(List<User> list);
    void delete(String key);
    void delete(List<User> list);
    boolean update(User user);
    User get(String keyId);
}


