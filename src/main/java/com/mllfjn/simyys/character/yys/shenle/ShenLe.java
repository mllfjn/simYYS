package com.mllfjn.simyys.character.yys.shenle;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterType;

public class ShenLe extends Character {
    public static final String privateName = "神乐";
    public ShenLe() {

    }

    @Override
    public void initSelf(int[] skillLevels) {
        setType(CharacterType.YYS);
        getSkills().put(1, new SkillPuGong(this, skillLevels[0]));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }
}
