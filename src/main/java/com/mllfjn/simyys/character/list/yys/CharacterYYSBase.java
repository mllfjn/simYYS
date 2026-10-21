package com.mllfjn.simyys.character.list.yys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.*;

import static com.mllfjn.simyys.character.PropertyKey.YYSSkillDesc;

public abstract class CharacterYYSBase extends Character {
    public YYSSkill[] yysSkills = getYYSSkills();

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        map.get(PropertyKey.GENERAL_SPEED_KEY).setValue("128");
        map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY).setValue("3126");
        map.get(PropertyKey.GENERAL_HP_KEY).setValue("27140");
        map.get(PropertyKey.GENERAL_DEFENSE_KEY).setValue("1051");
        map.get(PropertyKey.GENERAL_CRIT_RATE_KEY).setValue("0");
        map.get(PropertyKey.GENERAL_CRIT_POWER_KEY).setValue("150");
        map.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY).setValue("0");
        map.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY).setValue("0");

        map.put(PropertyKey.QI_LING_KEY, new PropertySelectSingle(PropertyKey.QI_LING_KEY));

        ((PropertyCheck) map.get(PropertyKey.GENERAL_YYS_KEY)).setValue(true);

        for (YYSSkill skill : yysSkills) {
            if (skill != null) {
                map.put(skill.name, new PropertyMultiInput(YYSSkillDesc)
                        .setValue(0, String.valueOf(skill.defaultLevel))
                        .setValue(1, String.valueOf(skill.defaultShuYin))
                );
            }
        }
        return map;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3256";
    }

    @Override
    protected void addOwnSkills() {}

    protected abstract YYSSkill[] getYYSSkills();

    public record YYSSkill(String name, int defaultLevel, int defaultShuYin) {}
}
