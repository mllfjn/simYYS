package com.mllfjn.simyys.character;

public class PropertyKey {
    public static final String GENERAL_SPEED_KEY = "通用_速度";
    public static final String GENERAL_BASE_ATTACK_KEY = "通用_基础攻击";
    public static final String GENERAL_YU_HUN_ATTACK_KEY = "通用_御魂攻击";
    public static final String GENERAL_TEAM_KEY = "通用_队伍";
    public static final String GENERAL_HP_KEY = "通用_生命";
    public static final String GENERAL_DEFENSE_KEY = "通用_防御";
    public static final String GENERAL_CRIT_RATE_KEY = "通用_暴击率";
    public static final String GENERAL_CRIT_POWER_KEY = "通用_暴击伤害";
    public static final String GENERAL_EFFECT_HIT_RATE_KEY = "通用_效果命中";
    public static final String GENERAL_EFFECT_RESIST_RATE_KEY = "通用_效果抵抗";
    public static final String GENERAL_MOB_KEY = "通用_是否为怪物";
    public static final String GENERAL_YYS_KEY = "通用_是否为阴阳师";
    public static final String GENERAL_SUMMON_KEY = "通用_是否为召唤物";
    public static final String SKILL_KEY = "技能等级";
    public static final String JI_FENG_MO_CI_TIAO_KEY = "极逢魔增益词条";

    public static int getSkillLevel(int skill, int i) {
        return switch (i) {
            case 1 -> skill / 100;
            case 2 -> skill / 10 % 10;
            case 3 -> skill % 10;
            default -> 0;
        };
    }
}
