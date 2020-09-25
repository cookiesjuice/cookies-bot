package com.github.cookiesjuice.service;

import com.github.cookiesjuice.entity.Tag;

import java.util.List;

public interface DeepDanBooruService {
    /**
     * 评价一张图片
     */
    List<Tag> evaluate(String imgPath);
}
