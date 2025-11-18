package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill4TODO extends Skill {
    public static final String SkillName = "强力——钳鳌重击";

    public Skill4TODO(Character belongTo) {
        super(belongTo, 0, 3, 0, 4);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {

    }
}
