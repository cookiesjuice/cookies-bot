package com.github.cookiesjuice.cookiesbot.api.event.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.event.Event;
import com.github.cookiesjuice.cookiesbot.api.message.MessageChain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * <p>好友消息</p>
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FriendMessage extends Event {
    /**
     * <p>消息链，是一个消息对象构成的数组</p>
     */
    private MessageChain messageChain;

    /**
     * <p>发送者信息</p>
     */
    private Sender sender;

    public FriendMessage(@NonNull JSONObject jsonObject) {
        messageChain = MessageChain.parseMessageChain(jsonObject.getJSONArray("messageChain"));
        sender = new Sender(jsonObject.getJSONObject("sender"));
    }

    /**
     * <p>发送者信息</p>
     */
    @Data
    @AllArgsConstructor
    public static class Sender {
        /**
         * <p>发送者的QQ号码</p>
         */
        private long id;

        /**
         * <p>发送者的昵称</p>
         */
        private String nickname;

        /**
         * <p>发送者的备注</p>
         */
        private String remark;

        public Sender(JSONObject jsonObject) {
            id = jsonObject.getLong("id");
            nickname = jsonObject.getString("nickname");
            remark = jsonObject.getString("remark");
        }
    }
}
