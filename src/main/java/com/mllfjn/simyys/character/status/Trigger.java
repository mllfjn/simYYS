package com.mllfjn.simyys.character.status;

public enum Trigger {
    BEFORE_ROUND, // 回合前
    AFTER_ROUND_FIRST, // 回合后,最早执行,适用于伤害
    AFTER_ROUND, // 回合后
    AFTER_ATTACK, // 遭到攻击
    ADDING_CROWD_CONTROL, // 要被控制时
    CAUSE_ATTACK, // 造成伤害时
    USED_SKILL, // 使用了技能（不包括普攻）
    USE_PU_GONG, // 使用普攻
}
