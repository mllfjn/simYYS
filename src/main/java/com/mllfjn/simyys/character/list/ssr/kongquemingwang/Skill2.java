package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.character.skill.PassiveSkill;

class Skill2 extends PassiveSkill {
    public Skill2(KongQueMingWang belongTo, int level) {
        super(belongTo, "羽化", level, 2);
        belongTo.setStatusKongQueLing(new StatusKongQueLing(belongTo,
                level >= 2,
                level >= 3,
                level >= 4,
                level >= 5
        ));
    }
}
