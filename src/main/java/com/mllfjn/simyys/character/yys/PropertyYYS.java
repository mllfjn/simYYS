package com.mllfjn.simyys.character.yys;

import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyCheck;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;

public class PropertyYYS {
    public static void changeDefaultProperty(PropertiesMap map) {
        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("128");
        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("3256");
        ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).setValue("5568");
        ((PropertyInput) map.get(PropertyKey.GENERAL_HP_KEY)).setValue("27140");
        ((PropertyInput) map.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("1051");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY)).setValue("0");


        ((PropertyCheck) map.get(PropertyKey.GENERAL_YYS_KEY)).setValue(true);
    }
}
