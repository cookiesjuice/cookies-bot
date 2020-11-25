package com.github.cookiesjuice.cookiesbot.module.lang.dao.impl;

import com.csvreader.CsvReader;
import com.github.cookiesjuice.cookiesbot.config.lang.LangProperties;
import com.github.cookiesjuice.cookiesbot.module.lang.dao.LangDao;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Repository
public class LangDaoImpl implements LangDao {
    private static Map<String, Map<String, String>> lang;

    private final LangProperties langProperties;

    public LangDaoImpl(LangProperties langProperties) {
        this.langProperties = langProperties;
    }

    @Override
    public Map<String, Map<String, String>> read() {
        if (lang == null) {
            return reload();
        }
        return lang;
    }

    @Override
    public Map<String, Map<String, String>> reload() {
        InputStream loader = getClass().getClassLoader().getResourceAsStream(langProperties.getPath());
        assert loader != null;
        CsvReader csvReader = new CsvReader(loader, StandardCharsets.UTF_8);

        lang = new HashMap<>();
        try {
            csvReader.readRecord();
            String[] hLine = csvReader.getValues();
            for (int i = 1; i < hLine.length; i++) {
                lang.put(hLine[i], new HashMap<>());
            }

            while (csvReader.readRecord()) {
                String[] line = csvReader.getValues();
                String key = line[0];
                for (int i = 1; i < hLine.length; i++) {
                    String type = hLine[i];
                    String value = line[i].replaceAll("\\\\n", "\n");
                    lang.get(type).put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            csvReader.close();
        }

        return lang;
    }
}
