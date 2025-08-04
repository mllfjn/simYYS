package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

import java.util.Map;

public class DaYuan extends Character {
    public static final String privateName = "纺愿缘结神";
    public DaYuan() {

    }

    @Override
    public void initSelf(int[] skillLevels) {
        ObservableList<Skill> skills = getSkills();
        skills.add(new Skill1(this, skillLevels[0]));
        skills.add(new Skill2TODO(this));
        skills.add(new Skill3TODO(this, skillLevels[2]));
        skills.add(new Skill5(this));
        skills.add(new Skill6(this));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[]{5, 3};
    }
}
