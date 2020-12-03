package com.github.cookiesjuice.cookiesbot.module.cmd.service.impl;

import com.github.cookiesjuice.cookiesbot.config.setu.SetuProperties;
import com.github.cookiesjuice.cookiesbot.config.setu.UserProperties;
import com.github.cookiesjuice.cookiesbot.module.cmd.UnauthorizedException;
import com.github.cookiesjuice.cookiesbot.module.cmd.service.AuthService;
import com.github.cookiesjuice.cookiesbot.module.cmd.service.CmdService;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.Everyday;
import com.github.cookiesjuice.cookiesbot.module.setu.entity.User;
import com.github.cookiesjuice.cookiesbot.module.setu.service.SetuService;
import com.github.cookiesjuice.cookiesbot.module.setu.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CmdServiceImpl implements CmdService {

    private final AuthService authService;
    private final SetuService setuService;
    private final UserService userService;
    private final UserProperties userProperties;
    private final SetuProperties setuProperties;

    public CmdServiceImpl(AuthService authService,
                          SetuService setuService,
                          UserService userService,
                          UserProperties userProperties,
                          SetuProperties setuProperties) {
        this.authService = authService;
        this.setuService = setuService;
        this.userService = userService;
        this.userProperties = userProperties;
        this.setuProperties = setuProperties;
    }

    @Override
    public String handleCmd(String cmd, String[] args, Long userId) {
        try {
            switch (cmd.trim()) {
                case "reboot":
                    return reboot(userId);
                case "rm":
                    return rm(args, userId);
                case "exp":
                    return myExp(userId);
                case "changeExp":
                    return changeExp(args, userId);
                default:
                    return String.format("未知指令%s", cmd);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String reboot(Long userId) throws UnauthorizedException {
        authService.requireSudo(userId);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return "正在重启...";
    }

    private String rm(String[] args, Long userId) {
        List<Long> success = new ArrayList<>();
        List<Long> failure = new ArrayList<>();
        if (args.length == 0) return "参数不能为空喵~";
        for (String arg : args) {
            Long setuId = Long.parseLong(arg);
            if (!isUploader(setuId, userId)) {
                try {
                    authService.requireSudo(userId);
                } catch (UnauthorizedException e) {
                    failure.add(setuId);
                }
            }
            if (setuService.delete(setuId)) {
                success.add(setuId);
            } else {
                failure.add(setuId);
            }
        }
        String response = "";
        if (!success.isEmpty()) {
            response += success.toString() + "删除成功喵~";
        }
        if (!failure.isEmpty()) {
            response += failure.toString() + "删除失败喵~";
        }
        return response;
    }

    private boolean isUploader(Long setuId, Long userId) {
        return setuService.find(setuId).getUploadUser().getId() == userId;
    }

    private String myExp(Long userId) {
        User user = userService.find(userId);
        if (user == null) {
            return "找不到用户信息喵~";
        }
        long xp = user.getExp();
        int level = user.getLevel();
        String xpInfo = xp + "";
        if (level < userProperties.getLevelExp().length - 1) {
            int xpForNextLevel = userProperties.getLevelExp()[level + 1];
            xpInfo += "/" + xpForNextLevel;
        }
        Everyday today = userService.getToday(user);
        int count = today.getSetuCount();
        int max = setuProperties.getLevelSetuMaxOfDay()[level];
        return String.format("等级：%d\n经验：%s\n今日总数：%d/%d",
                level, xpInfo, count, max);
    }

    private String changeExp(String[] args, Long userId) throws UnauthorizedException {
        authService.requireSudo(userId);
        if (args.length < 2) {
            return "Usage: \n" +
                    "changeExp <userId> <exp>";
        }
        long id = Long.parseLong(args[0]);
        int exp = Integer.parseInt(args[1]);
        User user = userService.findOrSave(id);
        userService.changeExp(user, exp);
        user = userService.find(id);
        return String.format("用户%s的xp现在为%d(%s)", args[0], user.getExp(), args[1]);
    }
}
