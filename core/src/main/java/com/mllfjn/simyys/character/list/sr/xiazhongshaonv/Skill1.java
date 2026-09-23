package com.mllfjn.simyys.character.list.sr.xiazhongshaonv;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "流光";

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
