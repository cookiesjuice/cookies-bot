package com.github.cookiesjuice.cookiesbot.module.setu.entity;

import javax.persistence.*;

/**
 * 用户对涩图的评价
 */
@Entity
@Table(name = "setu_grade")
public class Grade {

    /**
     * 唯一id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分数
     */
    private int score;

    /**
     * 用户
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    /**
     * 涩图
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Setu setu;

    public Grade() {
    }

    public Grade(Long id, int score, User user, Setu setu) {
        this.id = id;
        this.score = score;
        this.user = user;
        this.setu = setu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Setu getSetu() {
        return setu;
    }

    public void setSetu(Setu setu) {
        this.setu = setu;
    }
}