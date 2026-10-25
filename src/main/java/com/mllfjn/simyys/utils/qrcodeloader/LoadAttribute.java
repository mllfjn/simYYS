package com.mllfjn.simyys.utils.qrcodeloader;

import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyRequire;

import java.util.function.Function;

public enum LoadAttribute {
    // 顺序不可改变!
    EFFECT_RESIST_RATE(0, 300, pm -> pm.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY)),
    CRIT_RATE(10, 220, pm -> pm.get(PropertyKey.GENERAL_CRIT_RATE_KEY)),
    ATTACK(2000, 20000, pm -> pm.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)),
    EFFECT_HIT_RATE(0, 300, pm -> pm.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY)),
    HP(10000, 100000, pm -> pm.get(PropertyKey.GENERAL_HP_KEY)),
    CRIT_POWER(150, 420, pm -> pm.get(PropertyKey.GENERAL_CRIT_POWER_KEY)),
    SPEED(100, 300, pm -> pm.get(PropertyKey.GENERAL_SPEED_KEY)),
    DEFENCE(100, 1200, pm -> pm.get(PropertyKey.GENERAL_DEFENSE_KEY)),
    ;
    public final float min;
    public final float max;
    public final Function<PropertiesMap, PropertyRequire> setter;

    LoadAttribute(float min, float max, Function<PropertiesMap, PropertyRequire> setter) {
        this.min = min;
        this.max = max;
        this.setter = setter;
    }
}
