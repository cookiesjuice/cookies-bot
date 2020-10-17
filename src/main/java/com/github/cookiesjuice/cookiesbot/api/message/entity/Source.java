package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * 消息源信息
 * Source类型永远为chain的第一个元素
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Source extends Message {
    /**
     * 消息类型
     */
    private final String type = "Source";

    /**
     * 消息的识别号，用于引用回复
     */
    private int id;

    /**
     * 时间戳
     */
    private int time;

    public Source(@NonNull JSONObject jsonObject) {
        id = jsonObject.getInteger("id");
        time = jsonObject.getInteger("time");
    }
}
