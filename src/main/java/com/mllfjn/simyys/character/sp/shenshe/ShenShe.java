package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.List;

public class ShenShe extends Character {
    public static final String privateName = "神堕八岐大蛇";

    private int skill1Level;
    private int skill2Level;
    private int skill3Level;
    public ShenShe() {}

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("4153");

        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue("555"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);

        int skillLevel = properties.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skillLevel, 1);
        skill2Level = PropertyKey.getSkillLevel(skillLevel, 2);
        skill3Level = PropertyKey.getSkillLevel(skillLevel, 3);
    }

    @Override
    public void addSkills() {
        super.addSkills();
        skills.add(new Skill1(this, skill1Level));
    }

}
