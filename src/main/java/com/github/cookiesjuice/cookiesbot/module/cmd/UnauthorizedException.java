package com.github.cookiesjuice.cookiesbot.module.cmd;

public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
        super();
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
