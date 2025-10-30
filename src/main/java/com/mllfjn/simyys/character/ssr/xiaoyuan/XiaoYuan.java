package com.mllfjn.simyys.character.ssr.xiaoyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;

public class XiaoYuan extends Character {
    public static final String privateName = "缘结神";
    private int skill1Level;
    public XiaoYuan() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("2198");
        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue("515"));
        return map;
    }

    @Override
    public void init(PropertiesMap properties, BattlePane bp) {
        super.init(properties, bp);
        int skill = properties.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skill, 1);
    }

    @Override
    public void addSkills() {
        super.addSkills();
        skills.add(new Skill1(this, skill1Level));
    }

}
