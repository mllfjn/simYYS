package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill3TODO extends Skill {
    public static final String privateName = "蜃气爆弹";

    public Skill3TODO(Character belongTo) {
        super(belongTo, 0, 3, 0);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {

    }
}
