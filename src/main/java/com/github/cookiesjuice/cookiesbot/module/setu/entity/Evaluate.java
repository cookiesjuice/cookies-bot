package com.github.cookiesjuice.cookiesbot.module.setu.entity;

import javax.persistence.*;

/**
 * 涩图的标签分析
 */
@Entity
@Table(name = "setu_evaluate")
public class Evaluate {

    /**
     * 分析的唯一id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 可信度
     */
    private double reliability;

    /**
     * 涩图
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Setu setu;

    /**
     * 标签
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Tag tag;

    public Evaluate() {
    }

    public Evaluate(Long id, double reliability, Setu setu, Tag tag) {
        this.id = id;
        this.reliability = reliability;
        this.setu = setu;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }

    public Setu getSetu() {
        return setu;
    }

    public void setSetu(Setu setu) {
        this.setu = setu;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
