package com.mllfjn.simyys.character.status;

public enum Trigger {
    BEFORE_ROUND, // 回合前
    AFTER_ROUND_FIRST, // 回合后,最早执行,适用于伤害  后来发现阴阳师机制不是这样的,和持续类状态一同遍历一遍就可以了
    AFTER_ROUND, // 回合后
    OUT_ROUND_ACTION, // 回合外行动开始
    AFTER_ACTION, // 行动后(包括回合外行动)
    BEFORE_ATTACK, // 遭到攻击前，比如奉海图
    BEING_ATTACKED, // 被攻击时 (出伤害之前)
    AFTER_ATTACK, // 遭到攻击 (有伤害) 其实这两分的不是很开 以后再改
    MAKING_CROWD_CONTROL, // 要对其他角色造成控制时
    ADDING_CROWD_CONTROL, // 要被控制时
    ADDING_DEBUFF, // 被添加DEBUFF时
    CAUSE_ATTACK, // 造成伤害时
    WILL_USE_SKILL, // 将要使用技能
    USED_SKILL, // 使用了技能（不包括普攻）
    WILL_USE_PU_GONG, // 将要使用普攻
    USED_PU_GONG, // 使用普攻
    LOCATION_CHANGE, // 行动条位置改变,发生在跑条,推条,拉条.必须发生实际行动条位置改变
    AFTER_HEAL, // 被治疗后
    HP_CHANGE, // 生命值改变，发生在受到伤害且打破护盾，失去生命，受到治疗和恢复并且不是满血
    DIE, // 死亡后
    KILLED_CHARACTER, // 击杀角色后
}
