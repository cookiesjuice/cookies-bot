package com.github.cookiesjuice.cookiesbot.module.setu.service;

import com.github.cookiesjuice.cookiesbot.module.setu.entity.Evaluate;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.Setu;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.User;

import java.io.File;
import java.util.List;

public interface SetuService {
    /**
     * 涩图库目录
     */
    String SETU_PATH = "setu";

    /**
     * 查找一张涩图,没有找到则返回null
     *
     * @param id id
     * @return setu | null
     */
    Setu find(long id);

    /**
     * 从涩图库中取出指定的涩图文件,文件不存在则返回null
     *
     * @param setu setu
     * @return file | null
     */
    File getFile(Setu setu);

    /**
     * 上传涩图
     *
     * @param user 上传用户
     * @param file 图片文件
     * @return setu
     */
    Setu upload(User user, File file);

    /**
     * 删除一张涩图
     *
     * @param setu 要删除的涩图
     * @return boolean
     */
    boolean delete(Setu setu);

    /**
     * 删除一张涩图
     *
     * @param id 要删除的涩图的id，存在则删除并返回true，否则返回false
     * @return boolean
     */
    boolean delete(Long id);

    /**
     * 随机num张涩图
     *
     * @param num 几张
     * @return setuList
     */
    List<Setu> random(int num);

    /**
     * 随机1张涩图，库中数量为0则返回null
     *
     * @return setu | null
     */
    Setu random();

    /**
     * 分析图片
     *
     * @param imgPath 图片路径
     * @return evaluate list
     */
    List<Evaluate> evaluate(String imgPath);

    /**
     * 对一张涩图评分，如果用户评价过次涩图则更新评分并返回上一次的评分
     *
     * @param user  user
     * @param setu  setu
     * @param score score
     * @return old score | null
     */
    int grade(User user, Setu setu, int score);
}
