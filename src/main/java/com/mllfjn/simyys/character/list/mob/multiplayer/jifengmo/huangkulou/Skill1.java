package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;

class Skill1 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "激怒";

//    private final StatusDWCount status;


    public Skill1(Character belongTo) {
        super(belongTo, -1, 1);
        // 直接写在毒雾里了
//        status = new StatusDWCount(belongTo);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
