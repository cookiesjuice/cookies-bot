package com.github.cookiesjuice.dao.impl;

import com.github.cookiesjuice.dao.Dao;
import com.github.cookiesjuice.dao.UserDao;
import com.github.cookiesjuice.entity.User;

import java.util.List;

/**
 * 用户数据访问接口实现类
 */
public class UserDaoImpl extends Dao implements UserDao {
    @Override
    public void saveUser(User user) {
        saveOrUpdate(user);
    }

    @Override
    public void saveAllUser(List<User> userList) {
        saveAll(userList);
    }

    @Override
    public void deleteUser(User user) {
        delete(user);
    }

    @Override
    public User findUserById(long id) {
        return findByLongId(User.class, id);
    }

    @Override
    public List<User> findAllUser() {
        return findAll(User.class);
    }
}
