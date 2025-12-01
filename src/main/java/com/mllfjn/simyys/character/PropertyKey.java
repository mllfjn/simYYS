package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.collections.StringGroup;

public class PropertyKey {
    public static final String GENERAL_SPEED_KEY = "速度";
    public static final String GENERAL_BASE_ATTACK_KEY = "基础攻击";
    public static final String GENERAL_YU_HUN_ATTACK_KEY = "额外攻击";
    public static final String GENERAL_HP_KEY = "生命";
    public static final String GENERAL_DEFENSE_KEY = "防御";
    public static final String GENERAL_CRIT_RATE_KEY = "暴击率";
    public static final String GENERAL_CRIT_POWER_KEY = "暴击伤害";
    public static final String GENERAL_EFFECT_HIT_RATE_KEY = "效果命中";
    public static final String GENERAL_EFFECT_RESIST_RATE_KEY = "效果抵抗";

    public static final String GENERAL_TEAM_KEY = "是否为敌方";
    public static final String GENERAL_MOB_KEY = "是否为怪物";
    public static final String GENERAL_YYS_KEY = "是否为阴阳师";
    public static final String GENERAL_SUMMON_KEY = "是否为召唤物";
    public static final String[] GENERAL_INPUT_KEYS = {
            GENERAL_SPEED_KEY,
            GENERAL_BASE_ATTACK_KEY,
            GENERAL_YU_HUN_ATTACK_KEY,
            GENERAL_HP_KEY,
            GENERAL_DEFENSE_KEY,
            GENERAL_CRIT_RATE_KEY,
            GENERAL_CRIT_POWER_KEY,
            GENERAL_EFFECT_HIT_RATE_KEY,
            GENERAL_EFFECT_RESIST_RATE_KEY,
    };
    public static final String[] GENERAL_CHECK_KEYS = {
            GENERAL_TEAM_KEY,
            GENERAL_MOB_KEY,
            GENERAL_YYS_KEY,
            GENERAL_SUMMON_KEY,
    };
    public static final String SKILL_KEY = "技能等级";
    public static final String JI_FENG_MO_CI_TIAO_KEY = "极逢魔增益词条";
    public static final String YU_HUN_KEY = "御魂";
    public static final String JUE_XING_KEY = "是否觉醒";
    public static final String QI_LING_KEY = "契灵";

    public static int getSkillLevel(int skill, int i) {
        return switch (i) {
            case 1 -> skill / 100;
            case 2 -> skill / 10 % 10;
            case 3 -> skill % 10;
            default -> 0;
        };
    }

    private static final String[] YYSSkillDesc = new String[]{"技能等级", "契灵术印等级"};
    public static PropertyMultiInput getYYSSkillPMI() {
        return new PropertyMultiInput(YYSSkillDesc).setValue(0, "5").setValue(1, "3");
    }
}
