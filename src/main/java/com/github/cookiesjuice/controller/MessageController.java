package com.github.cookiesjuice.controller;

import com.github.cookiesjuice.entity.Tag;
import com.github.cookiesjuice.response.Message;
import com.github.cookiesjuice.response.message.Image;
import com.github.cookiesjuice.response.message.PlainText;
import com.github.cookiesjuice.service.DeepDanBooruService;
import com.github.cookiesjuice.service.SetuService;
import com.github.cookiesjuice.service.TencentAPIService;

import java.io.File;
import java.util.List;

public class MessageController {

    private final SetuService setuService;
    private final TencentAPIService tencentAPIService;
    private final DeepDanBooruService deepDanBooruService;

    public MessageController(SetuService setuService, TencentAPIService tencentAPIService, DeepDanBooruService deepDanBooruService) {
        this.setuService = setuService;
        this.tencentAPIService = tencentAPIService;
        this.deepDanBooruService = deepDanBooruService;
    }

    public Message handlePlainMessage(String input) {
        if (input.contains("涩图")) {
            String[] cNumbers = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
            int n = 0;
            for (int i = 0; i < cNumbers.length; i++) {
                if (input.contains("涩图" + cNumbers[i] + "连")) {
                    n = i + 1;
                    break;
                }
            }
            if (n > 0) {
                Message message = new Message();
                for (int i = 0; i < n; i++) {
                    File image = setuService.randomSeTuFile();
                    if (image != null) {
                        message.put(new Image(image));
                    }
                }
                return message;
            } else {
                File image = setuService.randomSeTuFile();
                if (image != null) {
                    return new Message().put(new Image(image));
                }
            }
        }
        return null;
    }

    public Message handleAtMessage(String input) {
        String result = tencentAPIService.autoChat(input);
        return new Message().put(new PlainText(result));
    }

    public Message handleAtMessage(String input, String imgPath) {
        System.out.println(imgPath);
        if (input.contains("涩图分析") && imgPath != null) {
            Message message = new Message();
            List<Tag> tagList = deepDanBooruService.evaluate(imgPath);
            for (Tag tag : tagList) {
                message.put(new PlainText(tag.getName() + " : " + tag.getReliability() + "\n"));
            }
            return message;
        } else {
            return handleAtMessage(input);
        }
    }
}
