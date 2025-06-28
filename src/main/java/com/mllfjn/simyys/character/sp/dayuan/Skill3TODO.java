package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill3TODO extends Skill {
    public static final String privateName = "与世结缘";

    public Skill3TODO(Character belongTo, int level) {
        super(belongTo, level, 2, 0);
    }

    @Override
    public void setName() {
        this.name = privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        ((DaYuan)getBelongTo()).addShenLi(1);
    }
}
