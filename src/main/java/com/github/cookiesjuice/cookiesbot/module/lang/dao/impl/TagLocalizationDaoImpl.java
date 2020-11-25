package com.github.cookiesjuice.cookiesbot.module.lang.dao.impl;

import com.csvreader.CsvReader;
import com.github.cookiesjuice.cookiesbot.config.lang.LangProperties;
import com.github.cookiesjuice.cookiesbot.module.lang.dao.TagLocalizationDao;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TagLocalizationDaoImpl implements TagLocalizationDao {
    private static Map<String, String> translation;

    private final LangProperties langProperties;

    public TagLocalizationDaoImpl(LangProperties langProperties) {
        this.langProperties = langProperties;
    }

    @Override
    public Map<String, String> read() {
        if (translation == null) {
            return reload();
        }
        return translation;
    }

    @Override
    public Map<String, String> reload() {
        InputStream loader = getClass().getClassLoader().getResourceAsStream(langProperties.getLocalizationFilePath());
        assert loader != null;
        CsvReader csvReader = new CsvReader(loader, StandardCharsets.UTF_8);

        translation = new HashMap<>();
        try {
            csvReader.readRecord();
            while (csvReader.readRecord()) {
                String[] line = csvReader.getValues();
                if (line[1].length() > 0) {
                    translation.put(line[0], line[1]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            csvReader.close();
        }

        return translation;
    }
}
