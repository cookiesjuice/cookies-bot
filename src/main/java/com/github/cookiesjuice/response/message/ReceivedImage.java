package com.github.cookiesjuice.response.message;

import com.github.cookiesjuice.response.MessageContent;
import lombok.Getter;

public class ReceivedImage extends MessageContent {
    @Getter
    private String url;

    public ReceivedImage(String url) {
        this.url = url;
    }
}
