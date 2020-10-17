package com.github.cookiesjuice.cookiesbot.entity;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Tag {
    /**
     * 涩图id
     */
    private long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 可信度
     */
    private double reliability;

    /**
     * 拥有此标签的涩图
     */
    private Set<Setu> setus = new HashSet<>();
}
