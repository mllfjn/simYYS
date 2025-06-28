package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill2TODO extends Skill {
    public static final String privateName = "守缘刃";

    public Skill2TODO(Character belongTo) {
        super(belongTo, 1, 1, 0);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        getBelongTo().deleteState(StateFlag.privateName);
        getBelongTo().deleteState(StateSTChiTODO.privateName);
        getBelongTo().deleteState(StateSTQingTODO.privateName);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && getBelongTo().getState(StateFlag.privateName) != null;
    }
}
