package com.github.cookiesjuice.service.impl;

import com.github.cookiesjuice.entity.Tag;
import com.github.cookiesjuice.service.DeepDanBooruService;
import com.github.cookiesjuice.util.HttpUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeepDanBooruSerivceImpl implements DeepDanBooruService {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public List<Tag> evaluate(String imgPath) {
        String savedPath = UUID.randomUUID().toString();
        if (imgPath.contains("http")) {
            HttpUtils.downloadFile(imgPath, savedPath);
            imgPath = savedPath;
        }

        List<String> retList = new ArrayList<>();
        String[] cmds = {"D:\\Python\\Python38\\Scripts\\deepdanbooru.exe", "evaluate", imgPath, "--project-path", "DeepDanbooru-cookies-bot"};

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
            Tag t = new Tag();
            t.setName(tag);
            t.setReliability(data);
            tagList.add(t);
        }

        File savedImg = new File(savedPath);
        savedImg.delete();
        return tagList;
    }
}
