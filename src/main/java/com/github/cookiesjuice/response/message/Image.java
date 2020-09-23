package com.github.cookiesjuice.response.message;

import com.github.cookiesjuice.response.MessageContent;
import lombok.Getter;

import java.io.File;

public class Image extends MessageContent {

    @Getter
    private File file;

    public Image(String path) {
        this.file = new File(path);
    }

    public Image(File file) {
        this.file = file;
    }
}
