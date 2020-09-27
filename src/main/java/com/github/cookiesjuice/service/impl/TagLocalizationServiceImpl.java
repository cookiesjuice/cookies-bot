package com.github.cookiesjuice.service.impl;

import com.github.cookiesjuice.service.TagLocalizationService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TagLocalizationServiceImpl implements TagLocalizationService {

    private static final String LOCALIZATION_FILE_PATH = "src/main/resources/tags.csv";

    private final Map<String, String> translation;

    public TagLocalizationServiceImpl() throws Exception {
        translation = new HashMap<>();
        File file = new File(LOCALIZATION_FILE_PATH);
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            String[] line = s.split("\\s*,\\s*", 3);
            if(line[1].length() > 0){
                translation.put(line[0], line[1]);
            }
        }
    }

    @Override
    public String translate(String input) {
        String result = translation.get(input);
        if(result != null){
            return result;
        }
        return input;
    }
}
