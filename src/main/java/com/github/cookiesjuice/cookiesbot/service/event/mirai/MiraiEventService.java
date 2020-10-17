package com.github.cookiesjuice.cookiesbot.service.event.mirai;

import com.alibaba.fastjson.JSONObject;

public interface MiraiEventService {

    /**
     * 处理mirai上报的事件
     * @param jsonObject 事件内容
     */
    void handEvent(JSONObject jsonObject);

}
