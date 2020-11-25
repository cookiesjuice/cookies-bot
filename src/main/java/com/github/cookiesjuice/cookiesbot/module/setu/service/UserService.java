package com.github.cookiesjuice.cookiesbot.module.setu.service;


import com.github.cookiesjuice.cookiesbot.module.setu.entity.Everyday;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.Setu;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.User;

public interface UserService {
    /**
     * 通过id查找一个用户,如果没找到则返回null
     *
     * @param id id
     * @return user | null
     */
    User find(long id);

    /**
     * 通过id查找一个用户,如果没找到则保存此用户
     *
     * @param id id
     * @return user
     */
    User findOrSave(long id);

    /**
     * 给用户增加经验
     *
     * @param user user
     * @param exp  exp
     */
    void addExp(User user, long exp);

    /**
     * 获得用户今日数据
     *
     * @param user user
     * @return everyday
     */
    Everyday getToday(User user);

    /**
     * 今日发言计数+1
     *
     * @param everyday everyday
     */
    void addSpeakCount(Everyday everyday);

    /**
     * 今日获取涩图计数+1
     *
     * @param everyday everyday
     */
    void addSetuCount(Everyday everyday);

    /**
     * 今日获取涩图计数+num
     *
     * @param num      次数
     * @param everyday everyday
     */
    void addSetuCount(Everyday everyday, int num);

    /**
     * 收藏一张涩图，如果重复收藏则返回false
     *
     * @param user user
     * @param setu setu
     */
    boolean addFavorite(User user, Setu setu);
}
