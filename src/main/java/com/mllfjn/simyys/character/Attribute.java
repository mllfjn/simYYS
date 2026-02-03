package com.mllfjn.simyys.character;

import java.util.function.Function;

public enum Attribute {
    SPEED("速度", Character::getSpeed),
    LOCATION("行动条位置", Character::getLocation),
    HP("生命", Character::getHp),
    HP_PERCENT("生命百分比", character -> character.getHp() / character.getMaxHp() * 100),
    ATTACK("攻击", Character::getAttack),
    DEFENCE("防御", Character::getDefence),
    CRIT_RATE("暴击", Character::getCritRate),
    CRIT_POWER("爆伤", Character::getCritPower),
    EFFECT_HIT_RATE("命中", Character::getEffectHitRate),
    EFFECT_RESIST_RATE("抵抗", Character::getEffectResistRate),
    INIT_ATTACK("初始攻击", Character::getInitAttack),
    ZENG_SHANG("增伤", Character::getZengShang),
    JIAN_SHANG("减伤", Character::getJianShang),
    YI_SHANG("易伤", Character::getYiShang),
    MaxHP("最大生命值", Character::getMaxHp),
    IGNORE_DEFENCE("无视防御", Character::getIgnoreDefense),
    CRIT_RESIST("暴击抵抗", Character::getCritResist),
    XIE_ZHAN("协战", Character::getXieZhanProbability),
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
