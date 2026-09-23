package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.character.Character;

class Skill6 extends SkillJieYuan {
    private static final String SkillName = "胜天之缘·青";

    public Skill6(Character belongTo) {
        super(belongTo, 6);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    void jieYuan(Character target) {
        target.addStatus(new StatusSTQing((DaYuan) getBelongTo(), target));
    }
}
