package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * 戳一戳消息
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Poke extends Message {
    /**
     * 消息类型
     */
    private final String type = "Poke";

    /**
     * 戳一戳的类型
     */
    private String name;

    public Poke(@NonNull JSONObject jsonObject) {
        name = jsonObject.getString("name");
    }
}
