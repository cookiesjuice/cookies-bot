package com.github.cookiesjuice.dao.impl;

import com.github.cookiesjuice.dao.UserDao;
import com.github.cookiesjuice.entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * 用户数据访问接口实现类
 */
public class UserDaoImpl extends DaoImpl<User> implements UserDao {
    @Override
    public User findByLongId(Serializable id) {
        return super.findByLongId(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return super.findAll(User.class);
    }
}
