package com.github.cookiesjuice.cookiesbot.service.event.mirai.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.MiraiHttp;
import com.github.cookiesjuice.cookiesbot.api.event.entity.FriendMessage;
import com.github.cookiesjuice.cookiesbot.api.event.entity.GroupMessage;
import com.github.cookiesjuice.cookiesbot.api.event.entity.TempMessage;
import com.github.cookiesjuice.cookiesbot.api.message.MessageChain;
import com.github.cookiesjuice.cookiesbot.api.message.entity.At;
import com.github.cookiesjuice.cookiesbot.api.message.entity.Image;
import com.github.cookiesjuice.cookiesbot.api.message.entity.Plain;
import com.github.cookiesjuice.cookiesbot.api.message.entity.Source;
import com.github.cookiesjuice.cookiesbot.entity.Tag;
import com.github.cookiesjuice.cookiesbot.service.event.mirai.MiraiEventService;
import com.github.cookiesjuice.cookiesbot.service.moudle.DeepDanBooruService;
import com.github.cookiesjuice.cookiesbot.service.moudle.SetuService;
import com.github.cookiesjuice.cookiesbot.service.moudle.TagLocalizationService;
import com.github.cookiesjuice.cookiesbot.service.moudle.TencentAPIService;
import com.github.cookiesjuice.cookiesbot.util.ConfigInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class MiraiEventServiceImpl implements MiraiEventService {
    private final Logger logger = LoggerFactory.getLogger(MiraiEventService.class);

    private static final long qq = ConfigInfo.config.getLong("service_mirai_qq");
    private static final String authKey = ConfigInfo.config.getString("service_mirai_authKey");

    private String sessionKey;

    private final SetuService setuService;
    private final TencentAPIService tencentAPIService;
    private final DeepDanBooruService deepDanBooruService;
    private final TagLocalizationService tagLocalizationService;

    public MiraiEventServiceImpl(SetuService setuService, TencentAPIService tencentAPIService, DeepDanBooruService deepDanBooruService, TagLocalizationService tagLocalizationService) {
        this.setuService = setuService;
        this.tencentAPIService = tencentAPIService;
        this.deepDanBooruService = deepDanBooruService;
        this.tagLocalizationService = tagLocalizationService;
    }

    public void handEvent(JSONObject jsonObject) {
        if (!openSession()) return;

        String type = jsonObject.getString("type");
        switch (type) {
            case "FriendMessage":
                handFriendMessage(new FriendMessage(jsonObject));
                break;
            case "GroupMessage":
                handGroupMessage(new GroupMessage(jsonObject));
                break;
            case "TempMessage":
                handTempMessage(new TempMessage(jsonObject));
                break;
        }
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeSession();
        }
    }

    /**
     * 处理好友消息
     *
     * @param friendMessage 好友消息事件实体
     */
    private void handFriendMessage(FriendMessage friendMessage) {
        logger.debug("[handFriendMessage]->friendMessage={}", friendMessage);

        MessageChain replyMessageChain = new MessageChain("两个人单独交谈小曲奇会害羞的哒~");
        int messageID = reply(friendMessage, replyMessageChain);
//        MiraiHttp.recall(sessionKey, messageID);
    }

    /**
     * 处理群消息
     *
     * @param groupMessage 群消息事件实体
     */
    private void handGroupMessage(GroupMessage groupMessage) {
        logger.debug("[handGroupMessage]->handGroupMessage={}", groupMessage);

        GroupMessage.Sender sender = groupMessage.getSender();
        MessageChain receiveMessageChain = groupMessage.getMessageChain();

        //将所有普通文本消息拼成一条处理
        List<Plain> plainList = receiveMessageChain.getMessageListByType(Plain.class);
        StringBuilder builder = new StringBuilder();
        for (Plain plain : plainList) {
            builder.append(plain.getText());
        }
        String plaintext = builder.toString();

        //检查消息中是否有@bot
        List<At> atList = receiveMessageChain.getMessageListByType(At.class);
        boolean isAtMe = false;
        for (At at : atList) {
            if (at.getTarget() == qq) {
                isAtMe = true;
                break;
            }
        }

        //@bot消息处理
        if (isAtMe) {
            At at = new At(sender.getId(), sender.getMemberName());
            if (plaintext.contains("分析")) {
                List<Image> imageList = receiveMessageChain.getMessageListByType(Image.class);
                if (imageList.size() > 0) {
                    Image image = imageList.get(0);
                    reply(groupMessage, new MessageChain(at).put("小曲奇正在分析中~"));
                    List<Tag> tagList = deepDanBooruService.evaluate(image.getUrl());
                    StringBuilder tagString = new StringBuilder("\n");
                    for (Tag tag : tagList) {
                        tagString.append(tagLocalizationService.translate(tag.getName())).append(" : ").append(tag.getReliability()).append('\n');
                    }
                    reply(groupMessage, new MessageChain(at).put(tagString.toString()));
                }
            } else {
                String replyString = tencentAPIService.autoChat(plaintext);
                reply(groupMessage, new MessageChain(at).put(replyString));
            }
        } else {
            if (Pattern.matches("^涩图.连$", plaintext)) {
                reply(groupMessage, new MessageChain("啊啊啊~太...太多啦~小曲奇受不了啦~"));
            } else if (plaintext.contains("涩图")) {
                File img = setuService.randomSeTuFile();
                if (img != null) {
                    Image image = MiraiHttp.uploadImage(sessionKey, "group", img);
                    reply(groupMessage, new MessageChain(image));
                }
            }
        }
    }

    /**
     * 处理临时消息
     *
     * @param tempMessage 临时消息事件实体
     */
    private void handTempMessage(TempMessage tempMessage) {
        logger.debug("[handTempMessage]->tempMessage={}", tempMessage);

        MessageChain replyMessageChain = new MessageChain("两个人单独交谈小曲奇会害羞的哒~");
        reply(tempMessage, replyMessageChain);
    }

    private boolean openSession() {
        sessionKey = MiraiHttp.auth(authKey);
        return MiraiHttp.verifyOrRelease(sessionKey, qq, "verify");
    }

    private void closeSession() {
        MiraiHttp.verifyOrRelease(sessionKey, qq, "release");
    }

    private int reply(FriendMessage friendMessage, MessageChain messageChain) {
        messageChain.put("QwQ~");
        Source source = (Source) friendMessage.getMessageChain().getMessageChain().get(0);
        FriendMessage.Sender sender = friendMessage.getSender();
        return MiraiHttp.sendFriendMessage(sessionKey, sender.getId(), source.getId(), messageChain);
    }

    private int reply(GroupMessage groupMessage, MessageChain messageChain) {
        messageChain.put("QwQ~");
        Source source = (Source) groupMessage.getMessageChain().getMessageChain().get(0);
        GroupMessage.Sender sender = groupMessage.getSender();
        return MiraiHttp.sendGroupMessage(sessionKey, sender.getGroup().getId(), source.getId(), messageChain);
    }

    private int reply(TempMessage tempMessage, MessageChain messageChain) {
        messageChain.put("QwQ~");
        Source source = (Source) tempMessage.getMessageChain().getMessageChain().get(0);
        TempMessage.Sender sender = tempMessage.getSender();
        return MiraiHttp.sendTempMessage(sessionKey, sender.getId(), sender.getGroup().getId(), source.getId(), messageChain);
    }
}
