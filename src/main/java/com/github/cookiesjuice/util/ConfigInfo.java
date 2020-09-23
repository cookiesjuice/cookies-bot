package com.github.cookiesjuice.util;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigInfo {
    private static final String CONFIG_PATH = "src/main/resources/config.json";
    public static JSONObject config;

    static {

    }

    public static int init() {
        StringBuilder jsonString = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(CONFIG_PATH)), StandardCharsets.UTF_8))) {
            String thisLine;
            while ((thisLine = in.readLine()) != null) {
                jsonString.append(thisLine);
            }
            String string = jsonString.toString();
            config = JSONObject.parseObject(string);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
