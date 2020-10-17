package com.github.cookiesjuice.cookiesbot.service.moudle.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.service.moudle.TencentAPIService;
import com.github.cookiesjuice.cookiesbot.util.ConfigInfo;
import com.github.cookiesjuice.cookiesbot.util.HttpUtils;
import com.github.cookiesjuice.cookiesbot.util.MD5Utils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 腾讯AI开放平台接口
 */
@Service
public class TencentAPIServiceImpl implements TencentAPIService {
    /**
     * 智能闲聊
     *
     * @param question 闲聊消息内容
     * @return 回复的内容
     */
    public String autoChat(String question) {
        String reply = null;

        String url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
        String appid = ConfigInfo.config.getString("service_tencent_appid");
        String appkey = ConfigInfo.config.getString("service_tencent_appkey");

        Map<String, String> params = new HashMap<>();
        params.put("app_id", appid);
        params.put("time_stamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("nonce_str", String.valueOf(new Random().nextInt()));
        params.put("session", "1314520");
        params.put("question", question);
        params.put("sign", getSign(params, appkey));

        try {
            params.put("question", URLEncoder.encode(question, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String paramsstr = params.toString().replaceAll("[{}]", "").replace(", ", "&");
        byte[] retBytes = HttpUtils.doPost(url, paramsstr);
        if (retBytes != null) {
            String retString = new String(retBytes, StandardCharsets.UTF_8);
            JSONObject retJson = JSONObject.parseObject(retString);
            if (retJson.getInteger("ret") == 0) {
                reply = retJson.getJSONObject("data").getString("answer");
            } else {
                reply = "哎呀~小曲奇把消息弄丢了~";
            }
        }

        if (reply == null) {
            reply = "哎呀~小曲奇出错啦……请稍后再试~";
        }

        return reply;
    }

    /**
     * 获得根据参数与appkey计算接口鉴权签名
     *
     * @param params 参数
     * @param appkey appkey
     * @return 签名信息
     */
    private static String getSign(Map<String, String> params, String appkey) {
        StringBuilder paramstr = new StringBuilder();

        Object[] arrs = params.keySet().toArray();
        Arrays.sort(arrs);

        for (Object key : arrs) {
            try {
                paramstr.append(key).append("=").append(URLEncoder.encode(params.get(key.toString()), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        paramstr.append("app_key=").append(appkey);
        return MD5Utils.getMD5String(paramstr.toString());
    }
}
