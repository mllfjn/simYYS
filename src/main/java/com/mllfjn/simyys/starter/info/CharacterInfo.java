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
    public String criticalRate;
    public String criticalMultiplier;
    public String mingZhong;
    public String diKang;
    public List<String> special;

    public CharacterInfo(String name,
                         String speed,
                         String baseAttack,
                         String yuHunAttack,
                         String team,
                         String hp,
                         String defense,
                         String criticalRate,
                         String criticalMultiplier,
                         String mingZhong,
                         String diKang,
                         List<String> special) {
        this.name = name;
        this.speed = speed;
        this.baseAttack = baseAttack;
        this.yuHunAttack = yuHunAttack;
        this.team = team;
        this.hp = hp;
        this.defense = defense;
        this.criticalRate = criticalRate;
        this.criticalMultiplier = criticalMultiplier;
        this.mingZhong = mingZhong;
        this.diKang = diKang;
        this.special = special;
    }
}
