package com.github.cookiesjuice.cookiesbot.module.setu.service;

import com.github.cookiesjuice.cookiesbot.module.setu.entity.Evaluate;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.Setu;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.User;

import java.io.File;
import java.util.List;

public interface ImageService {

    /**
     * 将用户信息转为图片
     *
     * @param user user
     * @return 图片文件
     */
    File user(User user);

    /**
     * 将涩图信息转为图片
     *
     * @param setu setu
     * @return 图片文件
     */
    File setu(Setu setu);

    /**
     * 将分析信息转为图片
     *
     * @param evaluateList 分析列表
     * @return 图片文件
     */
    File evaluates(List<Evaluate> evaluateList);
}
