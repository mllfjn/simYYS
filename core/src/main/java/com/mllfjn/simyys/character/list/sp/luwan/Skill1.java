package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "麓影·蚀";

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        super.usePrivate(interactive, target);
        StatusLuShi.addStatus(getBelongTo(), target);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
