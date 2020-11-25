package com.github.cookiesjuice.cookiesbot.config.lang;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.lang")
public class LangProperties {
    /**
     * 语言类型
     */
    private String type;

    /**
     * 语言包文件位置
     */
    private String path;

    /**
     * 是否开启标签本地化
     */
    private boolean onTagLocalization;

    /**
     * 标签本地化文件位置
     */
    private String localizationFilePath;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isOnTagLocalization() {
        return onTagLocalization;
    }

    public void setOnTagLocalization(boolean onTagLocalization) {
        this.onTagLocalization = onTagLocalization;
    }

    public String getLocalizationFilePath() {
        return localizationFilePath;
    }

    public void setLocalizationFilePath(String localizationFilePath) {
        this.localizationFilePath = localizationFilePath;
    }
}
