package com.mllfjn.simyys.starter.info;

import java.util.List;

public class CharacterInfo {
    public String name;
    public String speed;
    public String baseAttack;
    public String yuHunAttack;
    public String team;
    public String hp;
    public String defense;
    public String critRate;
    public String critPower;
    public String effectHitRate;
    public String effectResistRate;
    public List<String> special;

    public CharacterInfo(String name,
                         String speed,
                         String baseAttack,
                         String yuHunAttack,
                         String team,
                         String hp,
                         String defense,
                         String critRate,
                         String critPower,
                         String effectHitRate,
                         String effectResistRate,
                         List<String> special) {
        this.name = name;
        this.speed = speed;
        this.baseAttack = baseAttack;
        this.yuHunAttack = yuHunAttack;
        this.team = team;
        this.hp = hp;
        this.defense = defense;
        this.critRate = critRate;
        this.critPower = critPower;
        this.effectHitRate = effectHitRate;
        this.effectResistRate = effectResistRate;
        this.special = special;
    }
}
