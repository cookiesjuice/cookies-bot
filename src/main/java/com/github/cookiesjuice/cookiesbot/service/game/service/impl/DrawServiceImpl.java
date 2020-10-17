package com.github.cookiesjuice.cookiesbot.service.game.service.impl;

import com.github.cookiesjuice.cookiesbot.service.game.entity.DrawResult;
import com.github.cookiesjuice.cookiesbot.service.game.entity.card.Card;
import com.github.cookiesjuice.cookiesbot.service.game.entity.equipment.Equipment;
import com.github.cookiesjuice.cookiesbot.service.game.service.DrawService;

import java.util.Random;

public class DrawServiceImpl implements DrawService {
    private static final double CARD_PROB = 0.1;

    private final Random random;

    public DrawServiceImpl(){
        this.random = new Random();
    }

    // TODO placeholder method
    @Override
    public DrawResult draw() {
        if(random.nextDouble() < CARD_PROB) {
            return new Card() {};
        }else {
            return new Equipment() {};
        }
    }
}
