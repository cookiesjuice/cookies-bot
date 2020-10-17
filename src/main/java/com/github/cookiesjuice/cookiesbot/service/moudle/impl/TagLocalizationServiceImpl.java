package com.github.cookiesjuice.cookiesbot.service.moudle.impl;

import com.github.cookiesjuice.cookiesbot.service.moudle.TagLocalizationService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
public class TagLocalizationServiceImpl implements TagLocalizationService {

    private static final String LOCALIZATION_FILE_PATH = "tags.csv";

    private final Map<String, String> translation;

    public TagLocalizationServiceImpl() {
        InputStream loader = getClass().getClassLoader().getResourceAsStream(LOCALIZATION_FILE_PATH);
        translation = new HashMap<>();
        assert loader != null;
        Scanner scanner = new Scanner(loader);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] line = s.split("\\s*,\\s*", 3);
            if (line[1].length() > 0) {
                translation.put(line[0], line[1]);
            }
        }
        scanner.close();
    }

    @Override
    public String translate(String input) {
        String result = translation.get(input);
        if (result != null) {
            return result;
        }
        return input;
    }
}
