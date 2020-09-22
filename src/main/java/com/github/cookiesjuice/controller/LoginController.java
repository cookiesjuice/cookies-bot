package com.github.cookiesjuice.controller;

import com.alibaba.fastjson.JSON;
import com.github.cookiesjuice.response.LoginInfo;
import lombok.Getter;

import java.io.*;

public class LoginController {
    private static final String CONFIG_PATH = "src/main/resources/config.json";

    @Getter
    public final long id;

    @Getter
    public final String password;

    public LoginController() throws Exception {
        LoginInfo info = JSON.parseObject(new FileInputStream(new File(CONFIG_PATH)), LoginInfo.class);
        this.id = info.getId();
        this.password = info.getPassword();
    }

}
