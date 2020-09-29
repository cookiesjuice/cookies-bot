package com.github.cookiesjuice.entity;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 */
@Data
public class User {
    /**
     * 用户id
     */
    private long id;

    /**
     * 参与了评分的涩图
     */
    private Set<Grade> grades = new HashSet<>();

    /**
     * 喜欢的涩图
     */
    private Set<Setu> favoriteSetus = new HashSet<>();

    /**
     * 上传的涩图
     */
    private Set<Setu> uploadSetus = new HashSet<>();
}
