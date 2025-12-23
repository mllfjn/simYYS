package com.mllfjn.simyys.character;

import java.util.function.Function;

public enum Attribute {
    HP("生命", Character::getHp),
    HP_PERCENT("生命百分比", character -> character.getHp() / character.getMaxHp() * 100),
    ATTACK("攻击", Character::getAttack),
    DEFENCE("防御", Character::getDefence),
    SPEED("速度", Character::getSpeed),
    CRIT_RATE("暴击", Character::getCritRate),
    CRIT_POWER("爆伤", Character::getCritPower),
    EFFECT_HIT_RATE("命中", Character::getEffectHitRate),
    EFFECT_RESIST_RATE("抵抗", Character::getEffectResistRate),

    ZENG_SHANG("增伤", Character::getZengShang), // 增伤
    JIAN_SHANG("减伤", null),
    YI_SHANG("易伤", Character::getYiShang),
    IGNORE_DEFENCE("无视防御", Character::getIgnoreDefense),
    MaxHP("最大生命值", Character::getMaxHp),
    ;
    private final String text;
    private final Function<Character, Double> getter;

    Attribute(String text, Function<Character, Double> getter) {
        this.text = text;
        this.getter = getter;
    }

    public Function<Character, Double> getGetter() {
        return getter;
    }

    public String getText() {
        return text;
    }
}
