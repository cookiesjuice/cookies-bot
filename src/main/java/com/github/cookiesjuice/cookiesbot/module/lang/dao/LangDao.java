package com.github.cookiesjuice.cookiesbot.module.lang.dao;

import java.util.Map;

public interface LangDao {
    /**
     * 读取文件中所有内容，如果已经加载过则返回初次读取的内容
     *
     * @return 语言包的键值对
     */
    Map<String, Map<String, String>> read();

    /**
     * 重新加载文件
     *
     * @return 语言包的键值对
     */
    Map<String, Map<String, String>> reload();
}
