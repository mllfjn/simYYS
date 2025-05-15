package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.character.Character;

public class QianJi extends Character {
    public static final String privateName = "千姬";
    public QianJi() {

    }

    @Override
    public void initSelf(int[] skillLevels) {
        getSkills().add(new SkillPuGong(this, skillLevels[0]));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }
}
