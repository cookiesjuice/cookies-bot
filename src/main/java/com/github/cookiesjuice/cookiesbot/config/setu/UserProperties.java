package com.github.cookiesjuice.cookiesbot.config.setu;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.setu.user")
public class UserProperties {
    /**
     * 达到各个等级所需经验
     */
    private int[] levelExp;

    /**
     * 每次发言增加的经验
     */
    private int speakExp;

    /**
     * 每日发言经验增加的最大值
     */
    private int speakExpMaxOfDay;

    public int[] getLevelExp() {
        return levelExp;
    }

    public void setLevelExp(int[] levelExp) {
        this.levelExp = levelExp;
    }

    public int getSpeakExp() {
        return speakExp;
    }

    public void setSpeakExp(int speakExp) {
        this.speakExp = speakExp;
    }

    public int getSpeakExpMaxOfDay() {
        return speakExpMaxOfDay;
    }

    public void setSpeakExpMaxOfDay(int speakExpMaxOfDay) {
        this.speakExpMaxOfDay = speakExpMaxOfDay;
    }
}
