package com.github.cookiesjuice.cookiesbot.api.message.entity;

import com.github.cookiesjuice.cookiesbot.api.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ 所有人
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AtAll extends Message {
    /**
     * 消息类型
     */
    private final String type = "AtAll";
}
