package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;

class Skill1 extends PassiveSkill {
    private static final String SkillName = "激怒";



    public Skill1(Character belongTo) {
        super(belongTo, -1, 1);
        // 效果直接写在毒雾里了
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
