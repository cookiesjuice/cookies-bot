package com.github.cookiesjuice.entity;

import lombok.Data;

@Data
public class Tag {
    /**
     * 名字
     */
    private final String name;

    /**
     * 可信度
     */
    private final double reliability;


}
