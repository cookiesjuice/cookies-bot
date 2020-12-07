package com.github.cookiesjuice.cookiesbot.module.setu.service.impl;

import com.github.cookiesjuice.cookiesbot.config.setu.SetuProperties;
import com.github.cookiesjuice.cookiesbot.config.setu.UserProperties;
import com.github.cookiesjuice.cookiesbot.module.lang.service.TagLocalizationService;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.*;
import com.github.cookiesjuice.cookiesbot.module.setu.service.ImageService;
import com.github.cookiesjuice.cookiesbot.module.setu.service.UserService;
import com.github.cookiesjuice.cookiesbot.utils.ImageUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private final UserService userService;
    private final TagLocalizationService tagLocalizationService;
    private final UserProperties userProperties;
    private final SetuProperties setuProperties;

    public ImageServiceImpl(UserService userService, TagLocalizationService tagLocalizationService, UserProperties userProperties, SetuProperties setuProperties) {
        this.userService = userService;
        this.tagLocalizationService = tagLocalizationService;
        this.userProperties = userProperties;
        this.setuProperties = setuProperties;
    }

    @Override
    public File user(User user) {
        List<List<String>> lists = new ArrayList<>();
        lists.add(Collections.singletonList("基本信息"));
        StringBuilder table = new StringBuilder("<tr><th colspan='4'>基本信息</th></tr>\n");
        lists.add(Arrays.asList("ID", "等级", "经验", "涩币"));
        table.append("<tr>\n" + "\t<th>ID</th>\n" + "\t<th>等级</th>\n" + "\t<th>经验</th>\n" + "\t<th>涩币</th>\n" + "</tr>\n");
        String id = String.valueOf(user.getId());
        String level = String.valueOf(user.getLevel());
        String exp = String.valueOf(user.getExp());
        int[] levelExp = userProperties.getLevelExp();
        if (user.getLevel() < levelExp.length - 1) {
            exp += "/" + levelExp[user.getLevel() + 1];
        }
        String scoin = String.valueOf(user.getScoin());
        lists.add(Arrays.asList(id, level, exp, scoin));
        table.append("<tr>\n" + "\t<td>").append(id).append("</td>\n").append("\t<td>").append(level).append("</td>\n").append("\t<td>").append(exp).append("</td>\n").append("\t<td>").append(scoin).append("</td>\n").append("</tr>\n");

        lists.add(Arrays.asList("今日涩图", "今日上限"));
        table.append("<tr><th colspan='2'>今日涩图</th><th colspan='2'>今日上限</th></tr>\n");
        Everyday today = userService.getToday(user);
        String setuToday = String.valueOf(today.getSetuCount());
        String setuMax = String.valueOf(setuProperties.getLevelSetuMaxOfDay()[user.getLevel()]);
        lists.add(Arrays.asList(setuToday, setuMax));
        table.append("<tr>\n" + "\t<td colspan='2'>").append(setuToday).append("</td>\n").append("\t<td colspan='2'>").append(setuMax).append("</td>\n").append("</tr>\n");

        lists.add(Collections.singletonList("收藏的涩图"));
        table.append("<tr><th colspan='4'>收藏的涩图</th></tr>\n");
        lists.add(Collections.singletonList("涩图ID"));
        table.append("<tr>\n" + "\t<th colspan='4'>涩图ID</th>\n" + "</tr>\n");
        if (user.getFavoriteSetus().size() == 0) {
            lists.add(Collections.singletonList("无"));
            table.append("<tr>\n" + "\t<td colspan='4'>无</td>\n" + "</tr>\n");
        } else {
            for (Setu setu : user.getFavoriteSetus()) {
                String setuId = String.valueOf(setu.getId());
                lists.add(Collections.singletonList(setuId));
                table.append("<tr>\n" + "\t<td colspan='4'>").append(setuId).append("</td>\n").append("</tr>\n");
            }
        }

        lists.add(Collections.singletonList("参与的评分"));
        table.append("<tr><th colspan='4'>参与的评分</th></tr>\n");
        lists.add(Arrays.asList("涩图ID", "评分"));
        table.append("<tr>\n" + "\t<th colspan='2'>涩图ID</th>\n" + "\t<th colspan='2'>评分</th>\n" + "</tr>\n");
        if (user.getGrades().size() == 0) {
            lists.add(Collections.singletonList("无"));
            table.append("<tr>\n" + "\t<td colspan='4'>无</td>\n" + "</tr>\n");
        } else {
            for (Grade grade : user.getGrades()) {
                String gradeSetuId = String.valueOf(grade.getSetu().getId());
                String gradeScore = String.valueOf(grade.getScore());
                lists.add(Arrays.asList(gradeSetuId, gradeScore));
                table.append("<tr>\n" + "\t<td colspan='2'>").append(gradeSetuId).append("</td>\n").append("\t<td colspan='2'>").append(gradeScore).append("</td>\n").append("</tr>\n");
            }
        }

        return ImageUtils.arrToTabImage(lists, table.toString());
    }

    @Override
    public File setu(Setu setu) {
        List<List<String>> lists = new ArrayList<>();
        lists.add(Collections.singletonList("基本信息"));
        StringBuilder table = new StringBuilder("<tr><th colspan='4'>基本信息</th></tr>\n");
        lists.add(Arrays.asList("ID", "作者", "上传者ID", "上传时间"));
        table.append("<tr>\n" + "\t<th>ID</th>\n" + "\t<th>作者</th>\n" + "\t<th>上传者ID</th>\n" + "\t<th>上传时间</th>\n" + "</tr>\n");
        String id = setu.getId().toString();
        String author = setu.getAuthor();
        String uploadUserId = String.valueOf(setu.getUploadUser().getId());
        String uploadTime = setu.getUploadTime().toString();
        lists.add(Arrays.asList(id, author, uploadUserId, uploadTime));
        table.append("<tr>\n" + "\t<td>").append(id).append("</td>\n").append("\t<td>").append(author).append("</td>\n").append("\t<td>").append(uploadUserId).append("</td>\n").append("\t<td>").append(uploadTime).append("</td>\n").append("</tr>\n");

        lists.add(Collections.singletonList("评分详情"));
        table.append("<tr><th colspan='4'>评分详情</th></tr>\n");
        lists.add(Arrays.asList("用户ID", "评分"));
        table.append("<tr>\n" + "\t<th colspan='2'>用户ID</th>\n" + "\t<th colspan='2'>评分</th>\n" + "</tr>\n");
        if (setu.getGrades().size() == 0) {
            lists.add(Collections.singletonList("无"));
            table.append("<tr>\n" + "\t<td colspan='4'>无</td>\n" + "</tr>\n");
        } else {
            for (Grade grade : setu.getGrades()) {
                String gradeUserId = String.valueOf(grade.getUser().getId());
                String gradeScore = String.valueOf(grade.getScore());
                lists.add(Arrays.asList(gradeUserId, gradeScore));
                table.append("<tr>\n" + "\t<td colspan='2'>").append(gradeUserId).append("</td>\n").append("\t<td colspan='2'>").append(gradeScore).append("</td>\n").append("</tr>\n");
            }
        }

        lists.add(Collections.singletonList("喜爱此涩图的用户"));
        table.append("<tr><th colspan='4'>喜爱此涩图的用户</th></tr>\n");
        lists.add(Collections.singletonList("用户ID"));
        table.append("<tr>\n" + "\t<th colspan='4'>用户ID</th>\n" + "</tr>\n");
        if (setu.getFavoriteUsers().size() == 0) {
            lists.add(Collections.singletonList("无"));
            table.append("<tr>\n" + "\t<td colspan='4'>无</td>\n" + "</tr>\n");
        } else {
            for (User user : setu.getFavoriteUsers()) {
                String userId = String.valueOf(user.getId());
                lists.add(Collections.singletonList(userId));
                table.append("<tr>\n" + "\t<td colspan='4'>").append(userId).append("</td>\n").append("</tr>\n");
            }
        }

        return ImageUtils.arrToTabImage(lists, table.toString());
    }

    @Override
    public File evaluates(List<Evaluate> evaluateList) {
        List<List<String>> lists = new ArrayList<>();
        lists.add(Arrays.asList("标签名", "可信度"));
        for (Evaluate evaluate : evaluateList) {
            lists.add(Arrays.asList(tagLocalizationService.translate(evaluate.getTag().getName()), String.valueOf(evaluate.getReliability())));
        }

        return ImageUtils.arrToTabImage(lists);
    }
}
