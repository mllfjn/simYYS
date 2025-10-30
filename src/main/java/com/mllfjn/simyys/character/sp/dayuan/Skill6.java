package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;

class Skill6 extends SkillJieYuan {
    public static final String privateName = "胜天之缘·青";

    public Skill6(Character belongTo) {
        super(belongTo, 6);
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    void jieYuan(Character target) {
        target.addState(new StateSTQing((DaYuan) getBelongTo(), target));
    }
}
