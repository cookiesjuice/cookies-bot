package com.github.cookiesjuice.cookiesbot.module.setu.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 涩图实体类
 */
@Entity
@Table(name = "setu_setu")
public class Setu {
    /**
     * 涩图唯一id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 涩图名字
     */
    private String filename;

    /**
     * 作者
     */
    private String author = "";

    /**
     * 上传时间
     */
    private Date uploadTime = new Date();

    /**
     * 上传用户
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User uploadUser;

    /**
     * 此涩图的标签分析
     */
    @OneToMany(mappedBy = "setu", cascade = CascadeType.ALL)
    private List<Evaluate> evaluates = new ArrayList<>();

    /**
     * 用户的评分
     */
    @OneToMany(mappedBy = "setu", cascade = CascadeType.ALL)
    private List<Grade> grades = new ArrayList<>();

    /**
     * 喜爱此涩图的用户
     */
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "setu_user_favorite_setu",
            joinColumns = {@JoinColumn(name = "setu", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user", referencedColumnName = "id")})
    private List<User> favoriteUsers = new ArrayList<>();

    public Setu() {
    }

    public Setu(Long id, String filename, String author, Date uploadTime, User uploadUser, List<Evaluate> evaluates, List<Grade> grades, List<User> favoriteUsers) {
        this.id = id;
        this.filename = filename;
        this.author = author;
        this.uploadTime = uploadTime;
        this.uploadUser = uploadUser;
        this.evaluates = evaluates;
        this.grades = grades;
        this.favoriteUsers = favoriteUsers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public User getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(User uploadUser) {
        this.uploadUser = uploadUser;
    }

    public List<Evaluate> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<Evaluate> evaluates) {
        this.evaluates = evaluates;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<User> getFavoriteUsers() {
        return favoriteUsers;
    }

    public void setFavoriteUsers(List<User> favoriteUsers) {
        this.favoriteUsers = favoriteUsers;
    }
}
