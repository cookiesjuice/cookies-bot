package com.github.cookiesjuice.entity;

import lombok.Data;

@Data
public class Grade {
    /**
     * 唯一id
     */
    private long id;

    /**
     * 分数
     */
    private int number;

    /**
     * 用户
     */
    private User user;

    /**
     * 涩图
     */
    private Setu setu;
}
