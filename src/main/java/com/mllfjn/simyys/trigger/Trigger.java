package com.mllfjn.simyys.trigger;

public enum Trigger {
    BEFORE_ROUND, // 回合前
    AFTER_ROUND_FIRST, // 回合后,最早执行,适用于伤害
    AFTER_ROUND, // 回合后
    AFTER_ATTACK, // 遭到攻击
}
