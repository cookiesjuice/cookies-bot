package com.github.cookiesjuice.game.entity;


import com.github.cookiesjuice.game.entity.equipment.Equipment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Actor {
    protected double atk;
    protected double spd;
    protected double def;
    protected double hp;
    protected Prototype prototype;
    protected List<Equipment> equipments;

    private Random random;

    private static final double BASE_DEF = 100.0;

    public Actor(@NotNull Prototype prototype, List<Equipment> equipments) {
        this.atk = prototype.getAtk();
        this.spd = prototype.getSpd();
        this.def = prototype.getDef();
        this.hp = prototype.getHp();
        this.prototype = prototype;
        this.equipments = equipments;
    }

    public void doAttack(Actor other){
        for(Equipment equipment : this.equipments){
            equipment.beforeAttack(this, other);
        }
        for(Equipment equipment : other.equipments){
            equipment.beforeDefense(other, this);
        }
        if(other.def > 0){
            other.hp -= this.atk * BASE_DEF / (BASE_DEF + def) * getRandomAtkModifier();
        }else {
            other.hp -= this.atk * (BASE_DEF - 2 * def) / (BASE_DEF - def) * getRandomAtkModifier();
        }

        for(Equipment equipment : other.equipments){
            equipment.afterDefense(other, this);
        }

        for(Equipment equipment : this.equipments){
            equipment.afterAttack(this, other);
        }
    }

    private double getRandomAtkModifier() {
        final double sd = 0.05;
        return Math.max(0.0, random.nextGaussian() * sd + 1.0);
    }
}
