package com.github.cookiesjuice.controller;

import com.alibaba.fastjson.JSON;
import com.github.cookiesjuice.response.LoginInfo;
import lombok.Getter;

import java.io.*;

public class LoginController {
    private static final String CONFIG = "config.JSON";

    @Getter
    private final long id;

    @Getter
    private final String password;

    public LoginController() throws Exception {
        LoginInfo info = JSON.parseObject(new FileInputStream(new File(CONFIG)), LoginInfo.class);
        this.id = info.getId();
        this.password = info.getPassword();
    }

}
