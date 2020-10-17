package com.github.cookiesjuice.cookiesbot.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.cookiesjuice.cookiesbot.service.event.mirai.MiraiEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/event")
public class EventController {
    private final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final HttpServletRequest request;

    private final MiraiEventService miraiEventService;

    public EventController(HttpServletRequest request, MiraiEventService miraiEventService) {
        this.request = request;
        this.miraiEventService = miraiEventService;
    }

    @RequestMapping(path = "/mirai", method = RequestMethod.POST, produces = "application/json;charset=utf-8;")
    public void event(@RequestBody JSONObject jsonObject) {
        logger.info("{} {}", request.getContextPath(), jsonObject);

        miraiEventService.handEvent(jsonObject);
    }
}
