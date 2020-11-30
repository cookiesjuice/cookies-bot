package com.github.cookiesjuice.cookiesbot.module.cmd.service;

import com.github.cookiesjuice.cookiesbot.module.cmd.UnauthorizedException;

public interface AuthService {
    void requireSudo(Long id) throws UnauthorizedException;
}
