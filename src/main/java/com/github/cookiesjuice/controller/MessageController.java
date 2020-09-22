package com.github.cookiesjuice.controller;

import com.github.cookiesjuice.response.Message;
import com.github.cookiesjuice.service.SetuService;
import com.github.cookiesjuice.service.TencentAPIService;


import java.io.File;

public class MessageController {

    private final SetuService setuService;
    private final TencentAPIService tencentAPIService;

    public MessageController(SetuService setuService, TencentAPIService tencentAPIService){
        this.setuService = setuService;
        this.tencentAPIService = tencentAPIService;
    }

    public Message handlePlainMessage(String input){
        if(input.contains("涩图")){
            File image = setuService.randomSeTuFile();
            return new Message().put(image);
        }
        return null;
    }

    public Message handleAtMessage(String input){
        Message message = new Message();
        String result = tencentAPIService.autoChat(input);
        return message.put(result);
    }
}
