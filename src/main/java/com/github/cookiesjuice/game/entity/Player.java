package com.github.cookiesjuice.game.entity;

import com.github.cookiesjuice.game.entity.equipment.Equipment;
import lombok.Data;

import java.util.List;

@Data
public class Player {
    private Long id;
    private Prototype prototype;
    private List<Equipment> equipments;
}
