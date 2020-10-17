package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * @ 消息
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class At extends Message {
    /**
     * 消息类型
     */
    private final String type = "At";

    /**
     * 群员QQ号
     */
    private long target;

    /**
     * At时显示的文字，发送消息时无效，自动使用群名片
     */
    private String dispaly;

    public At(@NonNull JSONObject jsonObject) {
        target = jsonObject.getLong("target");
        dispaly = jsonObject.getString("dispaly");
    }
}
