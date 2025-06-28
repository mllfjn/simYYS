package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

class Skill6 extends SkillJieYuan {
    public static final String privateName = "胜天之缘·青";

    public Skill6(Character belongTo) {
        super(belongTo);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = getTarget(bp);
        lastUsedTarget = target.name;

        target.addState(new StateSTQingTODO(target, getBelongTo()));
    }
}
