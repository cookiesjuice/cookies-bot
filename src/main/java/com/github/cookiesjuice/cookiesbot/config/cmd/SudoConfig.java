package com.github.cookiesjuice.cookiesbot.config.cmd;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "cmd")
public class SudoConfig {
    private List<Long> sudoers;

    public void setSudoers(List<Long> sudoers) {
        this.sudoers = sudoers;
    }

    public List<Long> getSudoers() {
        return sudoers;
    }
}
