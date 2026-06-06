package com.mllfjn.simyys.character;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.collections.StringGroup;

public abstract class CharacterShiShenBase extends Character {
    public int skill1Level = 1;
    public int skill2Level = 1;
    public int skill3Level = 1;
    public boolean awakening;
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
        if (skillLevel < 100 || skillLevel > 999) {
            throw new IllegalArgumentException("技能等级输入错误");
        }
        skill3Level = skillLevel % 10;
        skillLevel /= 10;
        skill2Level = skillLevel % 10;
        skill1Level = skillLevel / 10;

        if (canAwakening()) {
            awakening = propertiesHolder.propertiesMap.get(PropertyKey.JUE_XING_KEY).getBoolean();
        }
    }

    protected abstract String getDefaultSkillLevel();
    protected abstract boolean canAwakening();
}
