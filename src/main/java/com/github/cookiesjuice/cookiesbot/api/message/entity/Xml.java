package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * Xml
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Xml extends Message {
    /**
     * 消息类型
     */
    private final String type = "Xml";

    /**
     * XML文本
     */
    private String xml;

    public Xml(@NonNull JSONObject jsonObject) {
        xml = jsonObject.getString("xml");
    }
}
