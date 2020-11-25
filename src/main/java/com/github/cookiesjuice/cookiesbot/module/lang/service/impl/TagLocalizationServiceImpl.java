package com.github.cookiesjuice.cookiesbot.module.lang.service.impl;

import com.github.cookiesjuice.cookiesbot.config.lang.LangProperties;
import com.github.cookiesjuice.cookiesbot.module.lang.dao.TagLocalizationDao;
import com.github.cookiesjuice.cookiesbot.module.lang.service.TagLocalizationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TagLocalizationServiceImpl implements TagLocalizationService {
    private final TagLocalizationDao tagLocalizationDao;
    private final LangProperties langProperties;

    public TagLocalizationServiceImpl(TagLocalizationDao tagLocalizationDao, LangProperties langProperties) {
        this.tagLocalizationDao = tagLocalizationDao;
        this.langProperties = langProperties;
    }

    @Override
    public String translate(String name) {
        if (langProperties.isOnTagLocalization()) {
            Map<String, String> translation = tagLocalizationDao.read();
            String result = translation.get(name);
            if (result != null) {
                return result;
            }
        }

        return name;
    }
}
