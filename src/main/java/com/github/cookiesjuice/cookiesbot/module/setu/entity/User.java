package com.github.cookiesjuice.cookiesbot.module.setu.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类
 */
@Entity
@Table(name = "setu_user")
public class User {
    /**
     * 用户id
     */
    @Id
    private long id;

    /**
     * 用户经验
     */
    private long exp;

    /**
     * 用户等级
     */
    private int level;

    /**
     * 管理员等级
     */
    private int adminLevel;

    /**
     * 涩币
     */
    private int scoin;

    /**
     * 参与了的涩图评分
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Grade> grades = new ArrayList<>();

    /**
     * 喜欢的涩图
     */
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "setu_user_favorite_setu",
            joinColumns = {@JoinColumn(name = "user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "setu", referencedColumnName = "id")})
    private List<Setu> favoriteSetus = new ArrayList<>();

    /**
     * 上传的涩图
     */
    @OneToMany(mappedBy = "uploadUser", cascade = CascadeType.ALL)
    private List<Setu> uploadSetus = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Everyday> everydays = new ArrayList<>();

    public User() {
    }

    public User(long id, long exp, int level, int adminLevel, int scoin, List<Grade> grades, List<Setu> favoriteSetus, List<Setu> uploadSetus, List<Everyday> everydays) {
        this.id = id;
        this.exp = exp;
        this.level = level;
        this.adminLevel = adminLevel;
        this.scoin = scoin;
        this.grades = grades;
        this.favoriteSetus = favoriteSetus;
        this.uploadSetus = uploadSetus;
        this.everydays = everydays;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }

    public int getScoin() {
        return scoin;
    }

    public void setScoin(int scoin) {
        this.scoin = scoin;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Setu> getFavoriteSetus() {
        return favoriteSetus;
    }

    public void setFavoriteSetus(List<Setu> favoriteSetus) {
        this.favoriteSetus = favoriteSetus;
    }

    public List<Setu> getUploadSetus() {
        return uploadSetus;
    }

    public void setUploadSetus(List<Setu> uploadSetus) {
        this.uploadSetus = uploadSetus;
    }

    public List<Everyday> getEverydays() {
        return everydays;
    }

    public void setEverydays(List<Everyday> everydays) {
        this.everydays = everydays;
    }
}
