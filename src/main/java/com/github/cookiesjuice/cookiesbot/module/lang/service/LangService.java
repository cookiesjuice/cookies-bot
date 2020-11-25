package com.github.cookiesjuice.cookiesbot.module.lang.service;

import com.github.cookiesjuice.cookiesbot.module.lang.Lang;

public interface LangService {

    /**
     * 获得语言包中的值
     *
     * @param key key
     * @return value
     */
    String getValue(Lang key);

}
