package com.mllfjn.simyys.starter.info;

import java.util.List;

public class CharacterInfo {
    public final String name;
    public final String speed;
    public final String baseAttack;
    public final String yuHunAttack;
    public final String team;
    public final String hp;
    public final String defense;
    public final String critRate;
    public final String critPower;
    public final String effectHitRate;
    public final String effectResistRate;
    public final List<String> special;

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
