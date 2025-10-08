package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;

public class NaMei extends Character {
    public static final String privateName = "伊邪那美";
    private int skill1Level;
    private int skill2Level;
    public NaMei() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("3618");
        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue("555"));
        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);
        int skillLevel = properties.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skillLevel, 1);
        skill2Level = PropertyKey.getSkillLevel(skillLevel, 2);
    }

    @Override
    public void addSkills() {
        super.addSkills();
        skills.add(new Skill1(this, skill1Level));
        skills.add(new Skill2TODO(this));
    }

    @Override
    public void useFrontSkill(BattlePane bp) {
        if (skill2Level == 5) {
            Skill2TODO skill2 = (Skill2TODO) getSkill(2);
            skill2.useFront(bp);
        }
    }
}
