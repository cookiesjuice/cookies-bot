package com.github.cookiesjuice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.service.TencentAPIService;
import com.github.cookiesjuice.util.HttpUtils;
import com.github.cookiesjuice.util.MD5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 腾讯AI开放平台接口
 */
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
        String appid = "2156830448";
        String appkey = "DZC8fa4hQUQ48cTT";

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
        System.out.println("post;url=" + url + ";params=" + paramsstr);
        byte[] retBytes = HttpUtils.doPost(url, paramsstr);
        if (retBytes != null) {
            String retString = new String(retBytes, StandardCharsets.UTF_8);
            JSONObject retJson = JSONObject.parseObject(retString);
            if (retJson.getInteger("ret") == 0) {
                reply = retJson.getJSONObject("data").getString("answer");
            } else {
                reply = "哎呀~麻花疼粑粑把你的消息丢掉啦……请重新试试吧~";
                System.out.println(retJson.toJSONString());
            }
        }

        if (reply == null) {
            reply = "哎呀~小曲奇出错啦……请稍后再试叭~";
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
