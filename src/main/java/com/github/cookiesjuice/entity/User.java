package com.github.cookiesjuice.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 */
public class User {
    /**
     * 用户id
     */
    private long id;

    /**
     * 喜欢的涩图
     */
    private Set<Setu> favoriteSetu = new HashSet<>();

    public User() {
    }

    public User(long id, Set<Setu> favoriteSetu) {
        this.id = id;
        this.favoriteSetu = favoriteSetu;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Setu> getFavoriteSetu() {
        return favoriteSetu;
    }

    public void setFavoriteSetu(Set<Setu> favoriteSetu) {
        this.favoriteSetu = favoriteSetu;
    }
}
