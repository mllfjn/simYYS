package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyCheck;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.propertygetter.PropertySelectSingle;
import com.mllfjn.simyys.collections.StringGroup;

public class YuanLaiGuang extends Character {
    public static final String CharacterName = "源赖光";

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("142");
        ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).setValue("5568");
        ((PropertyInput) map.get(PropertyKey.GENERAL_HP_KEY)).setValue("20826");
        ((PropertyInput) map.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("806");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("20");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY)).setValue("0");

        map.put(PropertyKey.QI_LING_KEY, new PropertySelectSingle(StringGroup.QI_LING));

        ((PropertyCheck) map.get(PropertyKey.GENERAL_YYS_KEY)).setValue(true);

        return map;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3256";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, 5, 3));
        addSkill(new Skill4(this, 5));
        addSkill(new Skill5(this, 5, 3));
    }
}
