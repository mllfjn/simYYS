package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;

class Skill7 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "坚韧";

    public Skill7(Character belongTo, int level, int skillID) {
        super(belongTo, level, skillID);
    }

    @Override
    public String getSkillDesc() {
        return "场上有白茧或小蜘蛛召唤物时,土蜘蛛受到的伤害降低70%";
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
