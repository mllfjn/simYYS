package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.character.Character;

public class NaMei extends Character {
    public static final String privateName = "伊邪那美";
    public NaMei() {

    }

    @Override
    public void initSelf(int[] skillLevels) {
        getSkills().put(1, new SkillPuGong(this, skillLevels[0]));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }
}
