package com.github.cookiesjuice.cookiesbot.service.moudle;

import com.github.cookiesjuice.cookiesbot.entity.Tag;

import java.util.List;

public interface DeepDanBooruService {
    /**
     * 评价一张图片
     */
    List<Tag> evaluate(String imgPath);
}
