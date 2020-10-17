package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * 语音消息
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Voice extends Message {
    /**
     * 消息类型
     */
    private final String type = "Voice";

    /**
     * <p>语音的voiceId，不为空时将忽略url属性</p>
     * <p>三个参数任选其一，出现多个参数时，按照voiceId > url > path的优先级</p>
     */
    private String voiceId;

    /**
     * <p>语音的URL，发送时可作网络语音的链接；接收时为腾讯语音服务器的链接，可用于语音下载</p>
     * <p>三个参数任选其一，出现多个参数时，按照voiceId > url > path的优先级</p>
     */
    private String url;

    /**
     * <p>语音的路径，发送本地语音，相对路径于plugins/MiraiAPIHTTP/voices</p>
     * <p>三个参数任选其一，出现多个参数时，按照voiceId > url > path的优先级</p>
     */
    private String path;

    public Voice(@NonNull JSONObject jsonObject) {
        voiceId = jsonObject.getString("voiceId");
        url = jsonObject.getString("url");
        path = jsonObject.getString("path");
    }
}
