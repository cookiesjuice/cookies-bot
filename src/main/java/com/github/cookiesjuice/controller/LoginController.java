package com.github.cookiesjuice.controller;

import com.github.cookiesjuice.util.ConfigInfo;
import lombok.Getter;

public class LoginController {

    @Getter
    public final long id;

    @Getter
    public final String password;

    public LoginController() {
        this.id = ConfigInfo.config.getInteger("bot_id");
        this.password = ConfigInfo.config.getString("bot_password");
    }

}
