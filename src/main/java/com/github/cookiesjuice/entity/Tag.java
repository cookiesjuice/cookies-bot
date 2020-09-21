package com.github.cookiesjuice.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 标签实体类
 */
public class Tag {
    /**
     * 标签id
     */
    private int id;

    /**
     * 标签名字
     */
    private String name;

    /**
     * 包含此标签的涩图
     */
    private Set<Setu> setuList = new HashSet<>();

    public Tag() {
    }

    public Tag(int id, String name, Set<Setu> setuList) {
        this.id = id;
        this.name = name;
        this.setuList = setuList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Setu> getSetuList() {
        return setuList;
    }

    public void setSetuList(Set<Setu> setuList) {
        this.setuList = setuList;
    }
}
