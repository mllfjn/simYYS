package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

public class NaMei extends Character {
    public static final String privateName = "伊邪那美";
    public NaMei() {

    }

    private boolean useFront;

    @Override
    public void initSelf(int[] skillLevels) {
        useFront = skillLevels[1] == 5;
        getSkills().add(new Skill1(this, skillLevels[0]));
        getSkills().add(new Skill2TODO(this));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[]{3};
    }

    @Override
    public void useFrontSkill(BattlePane bp) {
        if (useFront) {
            Skill2TODO skill2 = (Skill2TODO) getSkills().get(2);
            skill2.useFront(bp);
        }
    }
}
