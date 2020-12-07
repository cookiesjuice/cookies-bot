package com.github.cookiesjuice.cookiesbot.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

class ImageUtilsTest {
    @Test
    public void convertToImage() {
        String[][] arr = {{"一", "二啊啊啊啊啊啊啊", "三啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊", "四啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊"}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}, {"一二", "一", "一", " "}};

        File file = ImageUtils.arrToTabImage(arr);
        if (file != null) {
            System.out.println(file.getPath());
        }
    }
}