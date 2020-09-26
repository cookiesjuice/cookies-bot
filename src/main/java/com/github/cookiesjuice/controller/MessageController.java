package com.github.cookiesjuice.controller;

import com.github.cookiesjuice.entity.Tag;
import com.github.cookiesjuice.response.Message;
import com.github.cookiesjuice.response.message.Image;
import com.github.cookiesjuice.response.message.PlainText;
import com.github.cookiesjuice.service.DeepDanBooruService;
import com.github.cookiesjuice.service.SetuService;
import com.github.cookiesjuice.service.TencentAPIService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Map<String, Integer> inputNumbers = new HashMap<>();

            inputNumbers.put("一", 1);
            inputNumbers.put("二", 2);
            inputNumbers.put("两", 2);
            inputNumbers.put("三", 3);
            inputNumbers.put("四", 4);
            inputNumbers.put("五", 5);
            inputNumbers.put("六", 6);
            inputNumbers.put("七", 7);
            inputNumbers.put("八", 8);
            inputNumbers.put("九", 9);
            inputNumbers.put("十", 10);

            inputNumbers.put("1", 1);
            inputNumbers.put("2", 2);
            inputNumbers.put("3", 3);
            inputNumbers.put("4", 4);
            inputNumbers.put("5", 5);
            inputNumbers.put("6", 6);
            inputNumbers.put("7", 7);
            inputNumbers.put("8", 8);
            inputNumbers.put("9", 9);
            inputNumbers.put("10", 10);

            int n = 0;
            for (String numberKey : inputNumbers.keySet()) {
                if (input.contains("涩图" + numberKey + "连")) {
                    n = inputNumbers.get(numberKey);
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
