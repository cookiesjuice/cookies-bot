package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * Json
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Json extends Message {
    /**
     * 消息类型
     */
    private final String type = "Json";

    /**
     * Json文本
     */
    private String json;

    public Json(@NonNull JSONObject jsonObject) {
        json = jsonObject.getString("json");
    }
}
