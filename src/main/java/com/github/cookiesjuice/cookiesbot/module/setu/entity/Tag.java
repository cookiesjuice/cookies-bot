package com.github.cookiesjuice.cookiesbot.module.setu.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "setu_tag")
public class Tag {

    /**
     * 名字
     */
    @Id
    private String name;

    /**
     * 含有此标签的涩图分析
     */
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Evaluate> evaluates = new ArrayList<>();

    public Tag() {
    }

    public Tag(String name, List<Evaluate> evaluates) {
        this.name = name;
        this.evaluates = evaluates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Evaluate> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<Evaluate> evaluates) {
        this.evaluates = evaluates;
    }
}
