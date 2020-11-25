package com.github.cookiesjuice.cookiesbot.module.lang.service;

public interface TagLocalizationService {
    /**
     * 翻译一个标签
     *
     * @param name 标签的名字
     * @return 翻译后的结果
     */
    String translate(String name);
}
