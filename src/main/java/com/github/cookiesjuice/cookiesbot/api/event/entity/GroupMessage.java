package com.github.cookiesjuice.cookiesbot.api.event.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.event.Event;
import com.github.cookiesjuice.cookiesbot.api.message.MessageChain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * <p>群消息</p>
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupMessage extends Event {
    /**
     * <p>消息链，是一个消息对象构成的数组</p>
     */
    private MessageChain messageChain;

    /**
     * <p>发送者信息</p>
     */
    private Sender sender;

    public GroupMessage(@NonNull JSONObject jsonObject) {
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
         * <p>发送者的群名片</p>
         */
        private String memberName;

        /**
         * <p>发送者的群限权</p>
         */
        private String permission;

        /**
         * <p>消息发送群的信息</p>
         */
        private Group group;

        public Sender(JSONObject jsonObject) {
            id = jsonObject.getLong("id");
            memberName = jsonObject.getString("memberName");
            permission = jsonObject.getString("permission");
            group = new Group(jsonObject.getJSONObject("group"));
        }
    }

    /**
     * <p>消息发送群的信息</p>
     */
    @Data
    @AllArgsConstructor
    public static class Group {
        /**
         * <p>发送群的群号</p>
         */
        private long id;

        /**
         * <p>发送群的群名称</p>
         */
        private String name;

        /**
         * <p>发送群中，Bot的群限权</p>
         */
        private String permission;

        public Group(JSONObject jsonObject) {
            id = jsonObject.getLong("id");
            name = jsonObject.getString("name");
            permission = jsonObject.getString("permission");
        }
    }
}
