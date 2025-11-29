package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;

class Skill5 extends SkillJieYuan {
    public static final String SkillName = "胜天之缘·赤";

    public Skill5(Character belongTo) {
        super(belongTo, 5);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    void jieYuan(Character target) {
        target.addStatus(new StatusSTChi((DaYuan) getBelongTo(), target));
    }
}
