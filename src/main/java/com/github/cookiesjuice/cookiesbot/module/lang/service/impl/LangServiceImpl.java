package com.github.cookiesjuice.cookiesbot.module.lang.service.impl;

import com.github.cookiesjuice.cookiesbot.config.lang.LangProperties;
import com.github.cookiesjuice.cookiesbot.module.lang.Lang;
import com.github.cookiesjuice.cookiesbot.module.lang.dao.LangDao;
import com.github.cookiesjuice.cookiesbot.module.lang.service.LangService;
import org.springframework.stereotype.Service;

@Service
public class LangServiceImpl implements LangService {

    private final LangDao langDao;
    private final LangProperties langProperties;

    public LangServiceImpl(LangDao langDao, LangProperties langProperties) {
        this.langDao = langDao;
        this.langProperties = langProperties;
    }

    @Override
    public String getValue(Lang key) {
        return langDao.read().get(langProperties.getType()).get(key.name());
    }
}
