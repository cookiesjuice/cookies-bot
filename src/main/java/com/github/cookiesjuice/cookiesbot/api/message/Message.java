package com.github.cookiesjuice.cookiesbot.api.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.api.message.entity.*;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class Message {

    @Getter
    protected String type;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    public static Message parseMessage(@NonNull JSONObject jsonObject) {
        Message message;
        switch (jsonObject.getString("type")) {
            case "Source":
                message = new Source(jsonObject);
                break;
            case "Quote":
                message = new Quote(jsonObject);
                break;
            case "At":
                message = new At(jsonObject);
                break;
            case "AtAll":
                message = new AtAll();
                break;
            case "Face":
                message = new Face(jsonObject);
                break;
            case "Plain":
                message = new Plain(jsonObject);
                break;
            case "Image":
                message = new Image(jsonObject);
                break;
            case "FlashImage":
                message = new FlashImage(jsonObject);
                break;
            case "Voice":
                message = new Voice(jsonObject);
                break;
            case "Xml":
                message = new Xml(jsonObject);
                break;
            case "Json":
                message = new Json(jsonObject);
                break;
            case "App":
                message = new App(jsonObject);
                break;
            case "Poke":
                message = new Poke(jsonObject);
                break;
            default:
                message = null;
        }

        return message;
    }
}
