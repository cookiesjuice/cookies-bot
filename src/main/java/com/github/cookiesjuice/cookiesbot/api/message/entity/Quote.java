package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import com.github.cookiesjuice.cookiesbot.api.message.MessageChain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 引用回复
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Quote extends Message {
    /**
     * 消息类型
     */
    private final String type = "Quote";

    /**
     * 被引用回复的原消息的messageId
     */
    private int id;

    /**
     * 被引用回复的原消息所接收的群号，当为好友消息时为0
     */
    private long groupId;

    /**
     * 被引用回复的原消息的发送者的QQ号
     */
    private long senderId;

    /**
     * 被引用回复的原消息的接收者者的QQ号（或群号）
     */
    private long targetId;

    /**
     * 被引用回复的原消息的消息链对象
     */
    private MessageChain origin;

    public Quote(JSONObject jsonObject) {
        id = jsonObject.getInteger("id");
        groupId = jsonObject.getLong("groupId");
        senderId = jsonObject.getLong("senderId");
        targetId = jsonObject.getLong("targetId");
        origin = MessageChain.parseMessageChain(jsonObject.getJSONArray("origin"));
    }
}
