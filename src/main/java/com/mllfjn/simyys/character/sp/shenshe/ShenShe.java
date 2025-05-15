package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Character;

public class ShenShe extends Character {
    public static final String privateName = "神堕八岐大蛇";

    @Override
    public void initSelf(int[] skillLevels) {
        getSkills().add(new SkillPuGong(this, skillLevels[0]));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[]{2, 3};
    }
}
