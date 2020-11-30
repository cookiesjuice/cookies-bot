package com.github.cookiesjuice.cookiesbot.config.cmd;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.cmd")
public class SudoConfig {
    private List<Long> sudoers;

    public void setSudoers(List<Long> sudoers) {
        this.sudoers = sudoers;
    }

    public List<Long> getSudoers() {
        return sudoers;
    }
}
