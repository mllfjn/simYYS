package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;

class Skill3 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "天罗地网";

    public Skill3(Character belongTo) {
        super(belongTo, -1, 3);
    }

    @Override
    public String getSkillDesc() {
        return "受到一定(?)伤害时,有35%概率立即释放一次天罗地网";
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
