package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "纺缘";
    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        super.usePrivate(interactive, target);
        // lv5-获得1层神力
        if (getLevel() >= 5) {
            StatusShenLi.addStack(getBelongTo(), 1);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
