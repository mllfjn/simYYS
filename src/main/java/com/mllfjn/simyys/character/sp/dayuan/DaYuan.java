package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Map;

public class DaYuan extends Character {
    public static final String privateName = "纺愿缘结神";
    public DaYuan() {

    }

    @Override
    public void initSelf(int[] skillLevels) {
        Map<Integer, Skill> skills = getSkills();
        skills.put(1, new Skill1(this, skillLevels[0]));
        skills.put(2, new Skill2TODO(this));
        skills.put(3, new Skill3TODO(this, skillLevels[2]));
        skills.put(5, new Skill5(this));
        skills.put(6, new Skill6(this));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }
    public void addShenLi(int i) {
        StateShenLi shenLi = (StateShenLi) this.getState(StateShenLi.privateName);
        if (shenLi == null) {
            shenLi = new StateShenLi(this, this);
            this.addState(shenLi);
        }
        shenLi.addCeng(i);
    }
}
