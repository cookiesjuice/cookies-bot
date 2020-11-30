package com.github.cookiesjuice.cookiesbot.module.cmd.service.impl;

import com.github.cookiesjuice.cookiesbot.module.cmd.UnauthorizedException;
import com.github.cookiesjuice.cookiesbot.module.cmd.service.AuthService;
import com.github.cookiesjuice.cookiesbot.module.cmd.service.CmdService;
import com.github.cookiesjuice.cookiesbot.module.setu.service.SetuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CmdServiceImpl implements CmdService {

    private final AuthService authService;
    private final SetuService setuService;

    public CmdServiceImpl(AuthService authService, SetuService setuService) {
        this.authService = authService;
        this.setuService = setuService;
    }

    @Override
    public String handleCmd(String cmd, String[] args, Long userId) {
        try {
            switch (cmd) {
                case "reboot":
                    return reboot(userId);
                case "rm":
                    return rm(args, userId);
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

    private String rm(String[] args, Long userId) throws UnauthorizedException {
        List<Long> success = new ArrayList<>();
        List<Long> failure = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            Long setuId = Long.parseLong(args[i]);
            if (!isUploader(setuId, userId)) {
                authService.requireSudo(userId);
            }
            if (setuService.delete(setuId)) {
                success.add(setuId);
            } else {
                failure.add(setuId);
            }
        }
        String response = "";
        if (!success.isEmpty()) {
            response += success.toString() + "删除成功。";
        }
        if (!failure.isEmpty()) {
            response += failure.toString() + "删除失败。";
        }
        return response;
    }

    private boolean isUploader(Long setuId, Long userId) {
        return setuService.find(setuId).getUploadUser().getId() == userId;
    }
}
