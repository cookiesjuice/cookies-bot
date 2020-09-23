package com.github.cookiesjuice.dao;

import com.github.cookiesjuice.entity.User;

import java.util.List;

/**
 * 用户数据访问接口
 */
public interface UserDao {

    /**
     * 保存一个用户
     */
    void saveUser(User user);


    /**
     * 保存一组用户
     */
    void saveAllUser(List<User> userList);

    /**
     * 根据用户对象的id删除一个用户
     */
    void deleteUser(User user);

    /**
     * 根据用户id查找用户
     */
    User findUserById(long id);

    /**
     * 无条件查找所有用户
     */
    List<User> findAllUser();
}
