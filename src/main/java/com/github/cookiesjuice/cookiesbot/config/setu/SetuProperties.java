package com.github.cookiesjuice.cookiesbot.config.setu;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.setu.setu")
public class SetuProperties {
    /**
     * 各等级每天获得涩图的次数
     */
    private int[] levelSetuMaxOfDay;

    /**
     * 上传一张涩图增加的经验
     */
    private int uploadExp;

    public int[] getLevelSetuMaxOfDay() {
        return levelSetuMaxOfDay;
    }

    public void setLevelSetuMaxOfDay(int[] levelSetuMaxOfDay) {
        this.levelSetuMaxOfDay = levelSetuMaxOfDay;
    }

    public int getUploadExp() {
        return uploadExp;
    }

    public void setUploadExp(int uploadExp) {
        this.uploadExp = uploadExp;
    }
}
