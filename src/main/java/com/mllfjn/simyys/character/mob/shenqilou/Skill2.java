package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill2 extends Skill {
    private static final String SkillName = "钳鳌重击";

    public Skill2(Character belongTo) {
        super(belongTo, 0, 0, 0, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {

    }
}
