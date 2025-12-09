package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

public abstract class Skill1PuGongBase extends Skill {
    public Skill1PuGongBase(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 1);
    }

    @Override
    public void use(BattlePane bp) {
        useBase(bp, false);
        useDone();
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return true;
    }
}
