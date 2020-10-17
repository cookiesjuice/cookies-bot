package com.github.cookiesjuice.cookiesbot.api.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.entity.Plain;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MessageChain {

    @Getter
    private final List<Message> messageChain;

    public MessageChain() {
        messageChain = new ArrayList<>();
    }

    public MessageChain(Message message) {
        this();
        put(message);
    }

    public MessageChain(String message) {
        this();
        put(message);
    }

    public MessageChain put(String newMessage) {
        messageChain.add(new Plain(newMessage));
        return this;
    }

    public MessageChain put(Message newMessage) {
        messageChain.add(newMessage);
        return this;
    }

    public static MessageChain parseMessageChain(@NonNull JSONArray chainArr) {
        MessageChain messageChain = new MessageChain();

        for (int i = 0; i < chainArr.size(); i++) {
            JSONObject obj = chainArr.getJSONObject(i);
            Message message = Message.parseMessage(obj);
            if (message != null) {
                messageChain.put(message);
            }
        }

        return messageChain;
    }

    /**
     * 从消息列表中取出指定类型的消息
     *
     * @param tClass 消息类型
     * @return 消息列表
     */
    public <T extends Message> List<T> getMessageListByType(Class<T> tClass) {
        List<T> list = new ArrayList<>();
        for (Message message : messageChain) {
            if (message.getClass().equals(tClass)) {
                try {
                    list.add(tClass.cast(message));
                } catch (ClassCastException ignored) {
                }
            }
        }
        return list;
    }

    public String toJSONString() {
        return JSON.toJSONString(messageChain);
    }
}
