package com.github.cookiesjuice.response.message;

import com.github.cookiesjuice.response.MessageContent;
import lombok.Getter;

public class At extends MessageContent {

    @Getter
    private long qid;

    public At(Long qid) {
        this.qid = qid;
    }

}
