package com.github.cookiesjuice.cookiesbot.module.lang.dao;

import java.util.Map;

public interface TagLocalizationDao {

    /**
     * 读取文件中所有内容，如果已经加载过则返回初次读取的内容
     *
     * @return 本地化翻译的键值对
     */
    Map<String, String> read();

    /**
     * 重新加载文件
     *
     * @return 本地化翻译的键值对
     */
    Map<String, String> reload();

}
