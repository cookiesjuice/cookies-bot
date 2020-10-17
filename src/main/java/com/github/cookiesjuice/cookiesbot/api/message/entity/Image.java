package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

/**
 * 图片消息
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Image extends Message {
    /**
     * 消息类型
     */
    private final String type = "Image";

    /**
     * <p>图片的imageId，群图片与好友图片格式不同。不为空时将忽略url属性 </p>
     * <p>三个参数任选其一，出现多个参数时，按照imageId > url > path的优先级</p>
     */
    private String imageId;

    /**
     * <p>图片的URL，发送时可作网络图片的链接；接收时为腾讯图片服务器的链接，可用于图片下载</p>
     * <p>三个参数任选其一，出现多个参数时，按照imageId > url > path的优先级</p>
     */
    private String url;

    /**
     * <p>图片的路径，发送本地图片，相对路径于plugins/MiraiAPIHTTP/images</p>
     * <p>三个参数任选其一，出现多个参数时，按照imageId > url > path的优先级</p>
     */
    private String path;

    public Image(@NonNull JSONObject jsonObject) {
        imageId = jsonObject.getString("imageId");
        url = jsonObject.getString("url");
        path = jsonObject.getString("path");
    }
}
