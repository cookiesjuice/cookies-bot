package com.github.cookiesjuice.service.impl;

import com.github.cookiesjuice.entity.Tag;
import com.github.cookiesjuice.service.DeepDanBooruService;
import com.github.cookiesjuice.util.HttpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeepDanBooruSerivceImpl implements DeepDanBooruService {
    public List<Tag> evaluate(String imgPath) {
        if (imgPath.contains("http")) {
            HttpUtils.downloadFile(imgPath, "temp.setu");
            imgPath = "temp.setu";
        }

        List<String> retList = new ArrayList<>();
        String[] cmds = {"D:\\Python\\Python38\\Scripts\\deepdanbooru.exe", "evaluate", imgPath, "--project-path", "src/main/resources/DeepDanbooru-cookies-bot"};

        try {
            Process process = new ProcessBuilder(cmds).start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                retList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Tag> tagList = new ArrayList<>();

        for (int i = 1; i < retList.size() - 1; i++) {
            String tagData = retList.get(i);
            String[] tagDatas = tagData.split(" ");
            double data = Double.parseDouble(tagDatas[0].replaceAll("[()]", ""));
            String tag = tagDatas[1];
            tagList.add(new Tag(tag, data));
        }

        return tagList;
    }
}
