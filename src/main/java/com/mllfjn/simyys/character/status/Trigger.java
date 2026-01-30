package com.mllfjn.simyys.character.status;

public enum Trigger {
    BEFORE_ROUND, // 回合前
    AFTER_ROUND_FIRST, // 回合后,最早执行,适用于伤害
    AFTER_ROUND, // 回合后
    AFTER_ATTACK, // 遭到攻击
    MAKING_CROWD_CONTROL, // 要对其他角色造成控制时
    ADDING_CROWD_CONTROL, // 要被控制时
    CAUSE_ATTACK, // 造成伤害时
    WILL_USE_SKILL, // 将要使用技能
    USED_SKILL, // 使用了技能（不包括普攻）
    USE_PU_GONG, // 使用普攻
    INCREASE_LOCATION, // 被拉条,不论是否实际改变行动条位置
    LOCATION_CHANGE, // 行动条位置改变,发生在跑条,推条,拉条.必须发生实际行动条位置改变
    //    AFTER_HEAL, // 被治疗后
    HP_CHANGE, // 生命值改变，发生在受到伤害且打破护盾，失去生命，受到治疗和恢复并且不是满血
}
