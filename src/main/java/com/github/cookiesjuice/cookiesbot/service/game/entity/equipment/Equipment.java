package com.github.cookiesjuice.cookiesbot.service.game.entity.equipment;

import com.github.cookiesjuice.cookiesbot.service.game.entity.Actor;
import com.github.cookiesjuice.cookiesbot.service.game.entity.DrawResult;

public abstract class Equipment extends DrawResult {
    public void beforeAttack(Actor me, Actor other){

    }
    public void afterAttack(Actor me, Actor other) {

    }
    public void beforeDefense(Actor me, Actor other) {

    }
    public void afterDefense(Actor me, Actor other) {

    }
}
