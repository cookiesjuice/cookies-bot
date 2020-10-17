package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * 普通文本消息
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Plain extends Message {
    /**
     * 消息类型
     */
    private final String type = "Plain";

    /**
     * 文字消息
     */
    private String text;

    public Plain(@NonNull JSONObject jsonObject) {
        text = jsonObject.getString("text");
    }
}
