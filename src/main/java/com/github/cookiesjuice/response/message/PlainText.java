package com.github.cookiesjuice.response.message;

import com.github.cookiesjuice.response.MessageContent;
import lombok.Getter;

public class PlainText extends MessageContent {

    @Getter
    private String text;

    public PlainText(String text) {
        this.text = text;
    }

}
