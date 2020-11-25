package com.github.cookiesjuice.cookiesbot.module.setu.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户今日数据
 */
@Entity
@Table(name = "setu_everyday")
public class Everyday {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 日期
     */
    private Date date = new Date();

    /**
     * 用户
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    /**
     * 发言次数
     */
    private int speakCount;

    /**
     * 获得涩图次数
     */
    private int setuCount;

    public Everyday() {
    }

    public Everyday(Long id, Date date, User user, int speakCount, int setuCount) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.speakCount = speakCount;
        this.setuCount = setuCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSpeakCount() {
        return speakCount;
    }

    public void setSpeakCount(int speakCount) {
        this.speakCount = speakCount;
    }

    public int getSetuCount() {
        return setuCount;
    }

    public void setSetuCount(int setuCount) {
        this.setuCount = setuCount;
    }
}
