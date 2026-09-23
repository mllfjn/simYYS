package com.mllfjn.simyys.character.list.yys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.qiling.QiLingFactory;
import com.mllfjn.simyys.character.propertygetter.*;

public abstract class CharacterYYSBase extends Character {
    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("128");
        ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).setValue("3126");
        ((PropertyInput) map.get(PropertyKey.GENERAL_HP_KEY)).setValue("27140");
        ((PropertyInput) map.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("1051");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY)).setValue("0");

        map.put(PropertyKey.QI_LING_KEY, new PropertySelectSingle(QiLingFactory.QI_LING));

        ((PropertyCheck) map.get(PropertyKey.GENERAL_YYS_KEY)).setValue(true);

        return map;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3256";
    }

    @Override
    protected void addOwnSkills() {

    }
}
