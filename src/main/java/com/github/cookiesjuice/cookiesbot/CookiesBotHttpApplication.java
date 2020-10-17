package com.github.cookiesjuice.cookiesbot;

import com.github.cookiesjuice.cookiesbot.util.ConfigInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CookiesBotHttpApplication {

    public static void main(String[] args) {
        int initRet = ConfigInfo.init();
        if (initRet != 0) {
            System.out.println("Initialization configuration error! return code = $initRet");
        } else {
            SpringApplication.run(CookiesBotHttpApplication.class, args);
        }
    }

}
