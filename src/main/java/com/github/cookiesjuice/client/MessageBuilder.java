package com.github.cookiesjuice.client;

import com.github.cookiesjuice.response.MessageContent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

public class MessageBuilder {

    public static MessageChain buildFromGroupMessage(GroupMessageEvent groupMessage, com.github.cookiesjuice.response.Message message) {
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        MessageContent content = message.getHead();

        if (content != null) {
            chainBuilder.add(convertMessageToMirai(groupMessage, content));
            while ((content = content.getNext()) != null) {
                chainBuilder.add(convertMessageToMirai(groupMessage, content));
            }
        }

        return chainBuilder.build();
    }

    public static Message convertMessageToMirai(GroupMessageEvent groupMessage, com.github.cookiesjuice.response.MessageContent content) {
        Message message;

        if (content.getClass().equals(com.github.cookiesjuice.response.message.At.class)) {
            //@消息
            com.github.cookiesjuice.response.message.At at = (com.github.cookiesjuice.response.message.At) content;
            message = new At(groupMessage.getGroup().get(at.getQid()));

        } else if (content.getClass().equals(com.github.cookiesjuice.response.message.Image.class)) {
            //图片消息
            com.github.cookiesjuice.response.message.Image image = (com.github.cookiesjuice.response.message.Image) content;
            message = groupMessage.getGroup().uploadImage(image.getFile());

        } else if (content.getClass().equals(com.github.cookiesjuice.response.message.PlainText.class)) {
            //普通文本消息
            com.github.cookiesjuice.response.message.PlainText plainText = (com.github.cookiesjuice.response.message.PlainText) content;
            message = new PlainText(plainText.getText());

        } else {
            message = new PlainText("");
        }

        return message;
    }
}
