package com.github.cookiesjuice.cookiesbot.module.cmd.service.impl;

import com.github.cookiesjuice.cookiesbot.config.cmd.SudoConfig;
import com.github.cookiesjuice.cookiesbot.module.cmd.UnauthorizedException;
import com.github.cookiesjuice.cookiesbot.module.cmd.service.AuthService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService, InitializingBean {

    @Autowired
    private SudoConfig sudoConfig;

    private Set<Long> sudoers;

    @Override
    public void requireSudo(Long id) throws UnauthorizedException {
        if(!sudoers.contains(id)) {
            throw new UnauthorizedException("未授权");
        }
    }

    @Override
    public void afterPropertiesSet() {
        sudoers = new HashSet<>(sudoConfig.getSudoers());
    }
}
