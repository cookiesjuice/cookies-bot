package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * 表情
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Face extends Message {
    /**
     * 消息类型
     */
    private final String type = "Face";

    /**
     * QQ表情编号，可选，优先高于name
     */
    private int faceId;

    /**
     * QQ表情拼音，可选
     */
    private String name;

    public Face(@NonNull JSONObject jsonObject) {
        faceId = jsonObject.getInteger("faceId");
        name = jsonObject.getString("name");
    }
}
