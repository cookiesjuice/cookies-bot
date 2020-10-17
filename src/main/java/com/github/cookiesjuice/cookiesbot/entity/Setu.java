package com.github.cookiesjuice.cookiesbot.entity;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 涩图实体类
 */
@Data
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
     * 此涩图的标签
     */
    private Set<Tag> tags = new HashSet<>();

    /**
     * 用户的评分
     */
    private Set<Grade> grades = new HashSet<>();

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
}
