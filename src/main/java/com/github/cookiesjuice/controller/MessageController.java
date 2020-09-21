package com.github.cookiesjuice.controller;

import com.github.cookiesjuice.response.Message;
import com.github.cookiesjuice.service.impl.SetuServiceImpl;
import com.github.cookiesjuice.service.impl.TencentAPIMod;

import java.io.File;

public class MessageController {

    // TODO change to Service instead of implementation
    private SetuServiceImpl setuService = new SetuServiceImpl();

    public Message handlePlainMessage(String input){
        if(input.contains("涩图")){
            File image = setuService.randomSeTuFile();
            return new Message().put(image);
        }
        return null;
    }

    public Message handleAtMessage(String input){
        Message message = new Message();
        String result = TencentAPIMod.ZhiNengXianLiao(input);
        return message.put(result);
    }
}
