package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill4 extends Skill {
    public static final String SkillName = "强力——钳鳌重击";

    public Skill4(Character belongTo) {
        super(belongTo, 0, 0, 0, 4);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {

    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp);
    }
}
