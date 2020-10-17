package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * App
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class App extends Message {
    /**
     * 消息类型
     */
    private final String type = "App";

    /**
     * 内容
     */
    private String content;

    public App(@NonNull JSONObject jsonObject) {
        content = jsonObject.getString("content");
    }
}
