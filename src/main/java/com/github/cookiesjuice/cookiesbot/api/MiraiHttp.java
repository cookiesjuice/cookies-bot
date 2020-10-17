package com.github.cookiesjuice.cookiesbot.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.MessageChain;
import com.github.cookiesjuice.cookiesbot.api.message.entity.Image;
import com.github.cookiesjuice.cookiesbot.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MiraiHttp {
    private static final Logger logger = LoggerFactory.getLogger(MiraiHttp.class);

    private static final String HOST = "http://0.0.0.0:6666";

    /**
     * 使用此方法验证你的身份，并返回一个会话
     *
     * @param authKey 创建Mirai-Http-Server时生成的key，可在启动时指定或随机生成
     * @return 你的session key
     */
    public static String auth(String authKey) {
        logger.debug("[auth]->authKey={}", authKey);
        String path = "/auth";
        JSONObject params = new JSONObject();
        params.put("authKey", authKey);
        byte[] ret = HttpUtils.doPost(HOST + path, params.toJSONString());
        JSONObject retJsonObj = JSONObject.parseObject(new String(ret, StandardCharsets.UTF_8));
        logger.debug("retJsonObj={}", retJsonObj);
        int code = retJsonObj.getInteger("code");
        if (code == 0) {
            return retJsonObj.getString("session");
        } else {
            logger.error("Authentication failed! authKey={}", authKey);
            return null;
        }
    }

    /**
     * 激活或释放session
     *
     * @param sessionKey 你的session key
     * @param qq         Session将要绑定的Bot的qq号
     * @param operate    <p>verify: 使用此方法校验并激活你的Session，同时将Session与一个已登录的Bot绑定</p>
     *                   <p>release: 使用此方式释放session及其相关资源（Bot不会被释放） 不使用的Session应当被释放，长时间（30分钟）未使用的Session将自动释放，否则Session持续保存Bot收到的消息，将会导致内存泄露(开启websocket后将不会自动释放)</p>
     * @return 是否成功
     */
    public static boolean verifyOrRelease(String sessionKey, long qq, String operate) {
        logger.debug("[verifyOrRelease]->sessionKey={} & qq={}", sessionKey, qq);
        String path;
        if ("verify".equals(operate)) {
            path = "/verify";
        } else if ("release".equals(operate)) {
            path = "/release";
        } else {
            return false;
        }

        JSONObject params = new JSONObject();
        params.put("sessionKey", sessionKey);
        params.put("qq", qq);
        byte[] ret = HttpUtils.doPost(HOST + path, params.toJSONString());
        JSONObject retJsonObj = JSONObject.parseObject(new String(ret, StandardCharsets.UTF_8));
        logger.debug("retJsonObj={}", retJsonObj);
        return retJsonObj.getInteger("code") == 0;
    }

    /**
     * 发送消息
     *
     * @param path         消息类型的路径
     * @param sessionKey   已经激活的Session
     * @param quote        引用一条消息的messageId进行回复
     * @param messageChain 消息链，是一个消息对象构成的数组
     * @param params       各消息发送目标的参数
     * @return 消息id, 标识本条消息，用于撤回和引用回复
     */
    private static int sendMessage(String path, String sessionKey, int quote, MessageChain messageChain, JSONObject params) {
        params.put("sessionKey", sessionKey);
//        params.put("quote", quote);
        params.put("messageChain", JSONArray.parseArray(messageChain.toJSONString()));
        byte[] ret = HttpUtils.doPost(HOST + path, params.toJSONString());
        JSONObject retJsonObj = JSONObject.parseObject(new String(ret, StandardCharsets.UTF_8));
        logger.debug("retJsonObj={}", retJsonObj);
        int code = retJsonObj.getInteger("code");
        if (code == 0) {
            return retJsonObj.getInteger("messageId");
        } else {
            logger.error("sendMessage failed! path={} & code={}", path, code);
            return 0;
        }
    }

    /**
     * 发送好友消息
     *
     * @param sessionKey   已经激活的Session
     * @param target       发送消息目标好友的QQ号
     * @param quote        引用一条消息的messageId进行回复
     * @param messageChain 消息链，是一个消息对象构成的数组
     * @return 消息id, 标识本条消息，用于撤回和引用回复
     */
    public static int sendFriendMessage(String sessionKey, long target, int quote, MessageChain messageChain) {
        String path = "/sendFriendMessage";
        JSONObject params = new JSONObject();
        params.put("target", target);
        return sendMessage(path, sessionKey, quote, messageChain, params);
    }

    /**
     * 发送群消息
     *
     * @param sessionKey   已经激活的Session
     * @param target       发送消息目标群的群号
     * @param quote        引用一条消息的messageId进行回复
     * @param messageChain 消息链，是一个消息对象构成的数组
     * @return 消息id, 标识本条消息，用于撤回和引用回复
     */
    public static int sendGroupMessage(String sessionKey, long target, int quote, MessageChain messageChain) {
        String path = "/sendGroupMessage";
        JSONObject params = new JSONObject();
        params.put("target", target);
        return sendMessage(path, sessionKey, quote, messageChain, params);
    }

    /**
     * 发送临时会话消息
     *
     * @param sessionKey   已经激活的Session
     * @param qq           临时会话对象QQ号
     * @param group        临时会话群号
     * @param quote        引用一条消息的messageId进行回复
     * @param messageChain 消息链，是一个消息对象构成的数组
     * @return 消息id, 标识本条消息，用于撤回和引用回复
     */
    public static int sendTempMessage(String sessionKey, long qq, long group, int quote, MessageChain messageChain) {
        String path = "/sendTempMessage";
        JSONObject params = new JSONObject();
        params.put("qq", qq);
        params.put("group", group);
        return sendMessage(path, sessionKey, quote, messageChain, params);
    }

    /**
     * 撤回消息。对于bot发送的消息，有2分钟时间限制。对于撤回群聊中群员的消息，需要有相应权限
     *
     * @param sessionKey 已经激活的Session
     * @param target     需要撤回的消息的messageId
     * @return 是否成功
     */
    public static boolean recall(String sessionKey, int target) {
        String path = "/recall";
        JSONObject params = new JSONObject();
        params.put("sessionKey", sessionKey);
        params.put("target", target);
        byte[] ret = HttpUtils.doPost(HOST + path, params.toJSONString());
        JSONObject retJsonObj = JSONObject.parseObject(new String(ret, StandardCharsets.UTF_8));
        logger.debug("retJsonObj={}", retJsonObj);
        return retJsonObj.getInteger("code") == 0;
    }

    /**
     * 图片文件上传
     *
     * @param sessionKey 已经激活的Session
     * @param type       "friend" 或 "group" 或 "temp"
     * @param img        图片文件
     * @return Image
     */
    public static Image uploadImage(String sessionKey, String type, File img) {
        String path = "/uploadImage";
        Map<String, String> textBodys = new HashMap<>();
        Map<String, File> binaryBodys = new HashMap<>();
        textBodys.put("sessionKey", sessionKey);
        textBodys.put("type", type);
        binaryBodys.put("img", img);
        byte[] ret = HttpUtils.doPostByMultipart(HOST + path, textBodys, binaryBodys);
        JSONObject retJsonObj = JSONObject.parseObject(new String(ret, StandardCharsets.UTF_8));
        logger.debug("retJsonObj={}", retJsonObj);
        return new Image(retJsonObj);
    }
}
