package com.github.cookiesjuice.cookiesbot;

import com.github.cookiesjuice.cookiesbot.dao.Dao;
import com.github.cookiesjuice.cookiesbot.dao.impl.UserDaoImpl;
import com.github.cookiesjuice.cookiesbot.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HibernateTest {

    @Test
    public void testSaveUser() {
        Dao<User> userDao = new UserDaoImpl();
        User user = new User();
        user.setId(725577380);
        userDao.saveOrUpdate(user);
    }
}
