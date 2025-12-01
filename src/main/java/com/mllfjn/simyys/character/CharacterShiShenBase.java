package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.collections.StringGroup;

public abstract class CharacterShiShenBase extends Character {
    protected int skill1Level;
    protected int skill2Level;
    protected int skill3Level;
    protected boolean awakening;
    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        map.put(PropertyKey.YU_HUN_KEY, new PropertySelectMulti(StringGroup.YU_HUN));
        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue(getDefaultSkillLevel()));

        if (canAwakening()) {
            map.put(PropertyKey.JUE_XING_KEY, new PropertyCheck().setValue(true));
        }

        return map;
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        int skillLevel = propertiesHolder.propertiesMap.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skillLevel, 1);
        skill2Level = PropertyKey.getSkillLevel(skillLevel, 2);
        skill3Level = PropertyKey.getSkillLevel(skillLevel, 3);

        if (canAwakening()) {
            awakening = propertiesHolder.propertiesMap.get(PropertyKey.JUE_XING_KEY).getBoolean();
        }
    }

    protected abstract String getDefaultSkillLevel();
    protected abstract boolean canAwakening();
}
