package com.github.cookiesjuice.cookiesbot.module.cmd.service;

public interface CmdService {
    String handleCmd(String cmd, String[] args, Long userId);
}
