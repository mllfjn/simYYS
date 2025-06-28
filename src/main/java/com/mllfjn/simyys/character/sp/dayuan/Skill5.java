package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

class Skill5 extends SkillJieYuan {
    public static final String privateName = "胜天之缘·赤";

    public Skill5(Character belongTo) {
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

        target.addState(new StateSTChiTODO(target, getBelongTo()));
    }
}
