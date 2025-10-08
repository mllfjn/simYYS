package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.List;

public class DaYuan extends Character {
    public static final String privateName = "纺愿缘结神";
    private int skill1Level;
    private int skill3Level;
    public DaYuan() {

    }

    @Override
    protected boolean useSkillAuto(BattlePane bp) {
        return getSkill(5).tryUse(bp) ||
                getSkill(3).tryUse(bp);
    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("2224");

        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue("515"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);
        int skillLevel = properties.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skillLevel, 1);
        skill3Level = PropertyKey.getSkillLevel(skillLevel, 3);
    }

    @Override
    public void addSkills() {
        super.addSkills();
        skills.add(new Skill1(this, skill1Level));
        skills.add(new Skill2TODO(this));
        skills.add(new Skill3TODO(this, skill3Level));
        skills.add(new Skill5(this));
        skills.add(new Skill6(this));
    }
}
