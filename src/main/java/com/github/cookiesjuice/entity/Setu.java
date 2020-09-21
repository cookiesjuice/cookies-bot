package com.github.cookiesjuice.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 涩图实体类
 */
public class Setu {
    /**
     * 涩图唯一id
     */
    private long id;

    /**
     * 涩图名字
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 涩图标签
     */
    private Set<Tag> tags = new HashSet<>();

    /**
     * 喜爱此涩图的用户
     */
    private Set<User> favoriteUsers = new HashSet<>();

    /**
     * 上传用户
     */
    private User uploadUser;

    /**
     * 上传时间
     */
    private Date uploadTime;

    public Setu() {
    }

    public Setu(long id, String name, String author, Set<Tag> tags, Set<User> favoriteUsers, User uploadUser, Date uploadTime) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.tags = tags;
        this.favoriteUsers = favoriteUsers;
        this.uploadUser = uploadUser;
        this.uploadTime = uploadTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<User> getFavoriteUsers() {
        return favoriteUsers;
    }

    public void setFavoriteUsers(Set<User> favoriteUsers) {
        this.favoriteUsers = favoriteUsers;
    }

    public User getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(User uploadUser) {
        this.uploadUser = uploadUser;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
